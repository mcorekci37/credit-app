package com.emce.interestservice.config;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BatchScheduler {


	private final JobLauncher jobLauncher;
	private final Job job;

	@Scheduled(cron = "0 0 0 * * ?") // Schedule at midnight daily
//	@Scheduled(cron = "0 * * * * ?") // Schedule minutely
	public void performBatchJob() throws Exception {
		JobParameters params = new JobParametersBuilder()
				.addString("JobID", String.valueOf(System.currentTimeMillis()))
				.toJobParameters();
		jobLauncher.run(job, params);
	}
}
