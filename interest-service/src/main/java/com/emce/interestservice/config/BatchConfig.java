package com.emce.interestservice.config;

import com.emce.commons.entity.Installment;
import com.emce.interestservice.repository.InstallmentRepository;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDate;
import java.util.Collections;

@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class BatchConfig {

    private final JobRepository jobRepository;

    private final PlatformTransactionManager platformTransactionManager;
    private final InstallmentRepository installmentRepository;
    private final EntityManagerFactory entityManagerFactory;


    @Bean
    public JpaPagingItemReader<Installment> itemReader(EntityManagerFactory entityManagerFactory) {
        String jpqlQuery = "SELECT i FROM Installment i WHERE i.deadline < :currentDate AND i.dept > 0";

        JpaPagingItemReader<Installment> reader = new JpaPagingItemReader<>();
        reader.setEntityManagerFactory(entityManagerFactory);
        reader.setQueryString(jpqlQuery);
        reader.setParameterValues(Collections.singletonMap("currentDate", LocalDate.now()));
        reader.setPageSize(10);
        reader.setSaveState(true);

        return reader;
    }

    @Bean
    public ItemProcessor<Installment, Installment> itemProcessor() {
        return new InterestProcessor();
    }


    @Bean
    public RepositoryItemWriter<Installment> itemWriter() {
        RepositoryItemWriter<Installment> repositoryItemWriter = new RepositoryItemWriter<>();
        repositoryItemWriter.setRepository(installmentRepository);
        repositoryItemWriter.setMethodName("save");
        return repositoryItemWriter;
    }

    @Bean
    public Step step() {
        return new StepBuilder("interest-calculation", jobRepository)
                .<Installment, Installment>chunk(10, platformTransactionManager)
                .reader(itemReader(entityManagerFactory))
                .processor(itemProcessor())
                .writer(itemWriter())
                .build();
    }

    @Bean
    public Job job() {
        return new JobBuilder("calculateInterests", jobRepository)
                .start(step())
                .build();
    }

}
