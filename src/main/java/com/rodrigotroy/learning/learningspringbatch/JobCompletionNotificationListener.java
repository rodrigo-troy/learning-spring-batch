package com.rodrigotroy.learning.learningspringbatch;

import com.rodrigotroy.learning.learningspringbatch.domain.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class JobCompletionNotificationListener implements JobExecutionListener {
	private static final Logger LOG = LoggerFactory.getLogger(JobCompletionNotificationListener.class);

	private final JdbcTemplate jdbcTemplate;

	@Autowired
	public JobCompletionNotificationListener(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public void afterJob(JobExecution jobExecution) {
		if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
			LOG.info("!!! JOB FINISHED! Time to verify the results");

			jdbcTemplate.query("SELECT first_name, last_name FROM people",
							   (rs, row) -> new Person(
									   rs.getString(1),
									   rs.getString(2))
			).forEach(person -> LOG.info("Found <{{}}> in the database.",
										 person));
		}
	}

	@Override
	public void beforeJob(JobExecution jobExecution) {
		LOG.info("!!! JOB STARTED!");
	}
}
