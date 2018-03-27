package com.example.demobatch;

import java.util.OptionalLong;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.converter.JobParametersConverter;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BatchController {
	private final JobLauncher jobLauncher;
	private final JobExplorer jobExplorer;
	private final Job job;
	private final JobParametersConverter jobParametersConverter;
	private final JobRestartHelper jobRestartHelper;
	private final Logger log = LoggerFactory.getLogger(BatchController.class);

	public BatchController(JobLauncher jobLauncher, JobExplorer jobExplorer, Job job,
			JobParametersConverter jobParametersConverter,
			JobRestartHelper jobRestartHelper) {
		this.jobLauncher = jobLauncher;
		this.jobExplorer = jobExplorer;
		this.job = job;
		this.jobParametersConverter = jobParametersConverter;
		this.jobRestartHelper = jobRestartHelper;
	}

	@PostMapping
	@Scheduled(cron = "0 0 * * * *" /* every hour */)
	public String run() {
		JobParameters jobParameters = this.jobParametersConverter
				.getJobParameters(new Properties());
		try {
			JobExecution jobExecution = this.jobLauncher.run(this.job, jobParameters);
			return jobExecution.toString();
		}
		catch (Exception e) {
			log.error("Failed to restart jobParameters=" + jobParameters + " failed", e);
		}
		return "Failed";
	}

	@Scheduled(initialDelay = 0, fixedRate = Long.MAX_VALUE /* Never happen again */)
	public void tryRestart() {
		log.info("Trying to restart crashed jobs.");
		OptionalLong lastExecutionId = this.jobRestartHelper
				.lastJobExecutionIdOfLastUncompletedJobInstanceId();
		if (!lastExecutionId.isPresent()) {
			log.info("Nothing to restart.");
			return;
		}
		lastExecutionId.ifPresent(executionId -> {
			log.info("Restarting jobExecutionId={}", executionId);
			JobExecution lastJobExecution = this.jobExplorer.getJobExecution(executionId);
			BatchStatus status = lastJobExecution.getStatus();
			if (status == BatchStatus.STARTED) {
				this.jobRestartHelper.markFailed(executionId);
			}
			try {
				JobExecution jobExecution = this.jobLauncher.run(this.job,
						lastJobExecution.getJobParameters());
				log.info("Restarted jobExecutionId={}, jobExecution={}", executionId,
						jobExecution);
			}
			catch (Exception e) {
				log.error("Failed to restart jobExecutionId=" + executionId + " failed",
						e);
			}
		});
	}
}
