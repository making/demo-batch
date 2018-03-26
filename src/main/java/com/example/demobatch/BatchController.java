package com.example.demobatch;

import java.util.Properties;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.converter.JobParametersConverter;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BatchController {
	private final JobLauncher jobLauncher;
	private final Job job;
	private final JobParametersConverter jobParametersConverter;

	public BatchController(JobLauncher jobLauncher, Job job,
			JobParametersConverter jobParametersConverter) {
		this.jobLauncher = jobLauncher;
		this.job = job;
		this.jobParametersConverter = jobParametersConverter;
	}

	@PostMapping
	@Scheduled(cron = "0 0 * * * *")
	@Scheduled(initialDelay = 0, fixedRate = Long.MAX_VALUE /* Never happen */)
	public ExitStatus run() throws Exception {
		JobParameters jobParameters = this.jobParametersConverter
				.getJobParameters(new Properties());
		JobExecution jobExecution = this.jobLauncher.run(this.job, jobParameters);
		return jobExecution.getExitStatus();
	}
}
