package com.rodrigotroy.learning.learningspringbatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LearningSpringBatchApplication {
    private static final Logger LOG = LoggerFactory.getLogger(LearningSpringBatchApplication.class);

    public static void main(String[] args) {
        LOG.info("Starting LearningSpringBatchApplication");

        SpringApplication.run(LearningSpringBatchApplication.class,
                              args);
    }

}
