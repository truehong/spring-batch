package com.example.springbatch.batch;

import com.example.springbatch.domain.Dept;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;

/**
 * db to db
 * tasklet oriented
 * */
@Slf4j
@RequiredArgsConstructor
@Configuration
public class JpaPageJob1 {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory entityManagerFactory;

    private int chunkSize = 10;

    @Bean
    public Job JpaPageJob1_batchBuild() {
        return jobBuilderFactory.get("jpaPageJob1")
                .start(JpaPageJob1_step1())
                .build();
    }

    @Bean
    public Step JpaPageJob1_step1() {
        return stepBuilderFactory.get("JpaPageJob1_step1")
                .<Dept, Dept> chunk(chunkSize)
                .reader(jpaPageJob1_dbItemReader())
                .writer(jpaPageJob_printItemWriter())
                .build();
    }

    @Bean
    public JpaPagingItemReader<Dept> jpaPageJob1_dbItemReader() {
        return new JpaPagingItemReaderBuilder<Dept>()
                .name("jpaPageJob1_dbItemReader")
                .entityManagerFactory(entityManagerFactory)
                .pageSize(chunkSize)
                .queryString("SELECT d from Dept d order by dep_no asc")
                .build();
    }

    @Bean
    public ItemWriter<Dept> jpaPageJob_printItemWriter() {
        return list-> {
            for(Dept dept : list) {
                System.out.println(dept.toString());
                log.debug(dept.toString());
            }
        };
    }
}
