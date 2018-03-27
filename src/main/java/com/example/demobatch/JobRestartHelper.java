package com.example.demobatch;

import java.util.List;
import java.util.OptionalLong;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class JobRestartHelper {
	private final JobExplorer jobExplorer;
	private final JdbcTemplate jdbcTemplate;

	public JobRestartHelper(JobExplorer jobExplorer, JdbcTemplate jdbcTemplate) {
		this.jobExplorer = jobExplorer;
		this.jdbcTemplate = jdbcTemplate;
	}

	public OptionalLong lastJobExecutionIdOfLastUncompletedJobInstanceId() {
		try {
			Long jobExecutionId = this.jdbcTemplate.queryForObject(
					"SELECT JOB_EXECUTION_ID FROM BATCH_JOB_EXECUTION ORDER BY JOB_INSTANCE_ID DESC, JOB_EXECUTION_ID DESC LIMIT 1",
					Long.class);
			JobExecution jobExecution = this.jobExplorer.getJobExecution(jobExecutionId);
			if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
				return OptionalLong.empty();
			}
			return OptionalLong.of(jobExecutionId);
		}
		catch (EmptyResultDataAccessException e) {
			return OptionalLong.empty();
		}
	}

	@Transactional
	public void markFailed(long jobExecutionId) {
		List<Long> jobExecutionIds = this.jdbcTemplate.queryForList(
				"SELECT JOB_EXECUTION_ID FROM BATCH_JOB_EXECUTION WHERE JOB_INSTANCE_ID ="
						+ " (SELECT JOB_INSTANCE_ID FROM BATCH_JOB_EXECUTION WHERE JOB_EXECUTION_ID = ?)",
				Long.class, jobExecutionId);
		jobExecutionIds.forEach(id -> {
			this.jdbcTemplate.update(
					"UPDATE BATCH_JOB_EXECUTION SET STATUS=?, EXIT_CODE=?, VERSION=VERSION+1, END_TIME=NOW(), LAST_UPDATED=NOW() WHERE JOB_EXECUTION_ID=?",
					BatchStatus.FAILED.name(), ExitStatus.FAILED.getExitCode(), id);
			this.jdbcTemplate.update(
					"UPDATE BATCH_STEP_EXECUTION SET STATUS=?, EXIT_CODE=?, VERSION=VERSION+1, END_TIME=NOW(), LAST_UPDATED=NOW() WHERE JOB_EXECUTION_ID=?",
					BatchStatus.FAILED.name(), ExitStatus.FAILED.getExitCode(), id);
		});
	}
}
