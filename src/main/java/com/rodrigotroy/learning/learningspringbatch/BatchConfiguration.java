package com.rodrigotroy.learning.learningspringbatch;

import com.rodrigotroy.learning.learningspringbatch.domain.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;


@Configuration
public class BatchConfiguration {
    private static final Logger LOG = LoggerFactory.getLogger(BatchConfiguration.class);

    /**
     * This method defines a bean for the FlatFileItemReader, which reads input data from a CSV file and maps it to a Person object.
     *
     * @return A new instance of FlatFileItemReader<Person> with the necessary configurations
     */
    // tag::readerwriterprocessor[]
    @Bean
    public FlatFileItemReader<Person> getPersonFlatFileItemReader() {
        LOG.info("Creating a new instance of FlatFileItemReader<Person>");
        return new FlatFileItemReaderBuilder<Person>().name("personItemReader")
                                                      .resource(new ClassPathResource("sample-data.csv"))
                                                      .delimited()
                                                      .names("firstName",
                                                             "lastName")
                                                      .fieldSetMapper(new RecordFieldSetMapper())
                                                      .build();
    }

    /**
     * This method creates an instance of the PersonItemProcessor that you defined earlier, meant to convert the data to upper case.
     *
     * @return A new instance of PersonItemProcessor
     */
    @Bean
    public PersonItemProcessor getPersonItemProcessor() {
        LOG.info("Creating a new instance of PersonItemProcessor");
        return new PersonItemProcessor();
    }

    /**
     * This method creates an ItemWriter. This one is aimed at a JDBC destination and automatically gets a copy of the dataSource created by @EnableBatchProcessing. It includes the SQL statement needed to insert a single Person, driven by Java bean properties.
     *
     * @param dataSource
     * @return A new instance of JdbcBatchItemWriter<Person> with the necessary configurations
     */
    @Bean
    public JdbcBatchItemWriter<Person> writer(DataSource dataSource) {
        LOG.info("Creating a new instance of JdbcBatchItemWriter<Person>");
        return new JdbcBatchItemWriterBuilder<Person>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("INSERT INTO people (first_name, last_name) VALUES (:firstName, :lastName)")
                .dataSource(dataSource)
                .build();
    }
    // end::readerwriterprocessor[]

    // tag::jobstep[]

    /**
     * This method defines the job. Jobs are built from steps, where each step can involve a reader, a processor, and a writer.
     * In this job definition, you need an incrementer, because jobs use a database to maintain execution state. You then list each step, (though this job has only one step). The job ends, and the Java API produces a perfectly configured job.
     *
     * @param jobRepository
     * @param jobCompletionNotificationListener
     * @param step1
     * @return A new instance of Job with the necessary configurations
     */
    @Bean
    public Job importUserJob(JobRepository jobRepository,
                             JobCompletionNotificationListener jobCompletionNotificationListener,
                             Step step1) {
        LOG.info("Creating a new instance of Job");
        return new JobBuilder("importUserJob",
                              jobRepository)
                .incrementer(new RunIdIncrementer())
                .listener(jobCompletionNotificationListener)
                .flow(step1)
                .end()
                .build();
    }

    /**
     * This method defines a single step. Jobs are built from steps, where each step can involve a reader, a processor, and a writer.
     * In this step definition, you define how much data to write at a time. In this case, it writes up to ten records at a time. Next, you configure the reader, processor, and writer by using the beans injected earlier.
     *
     * @param jobRepository
     * @param transactionManager
     * @param writer
     * @return A new instance of Step with the necessary configurations
     */
    @Bean
    public Step step1(JobRepository jobRepository,
                      PlatformTransactionManager transactionManager,
                      JdbcBatchItemWriter<Person> writer) {
        LOG.info("Creating a new instance of Step");
        return new StepBuilder("step1",
                               jobRepository)
                .<Person, Person>chunk(10,
                                       transactionManager)
                .reader(getPersonFlatFileItemReader())
                .processor(getPersonItemProcessor())
                .writer(writer)
                .build();
    }
    // end::jobstep[]
}
