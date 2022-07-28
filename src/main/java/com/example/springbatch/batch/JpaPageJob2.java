package com.example.springbatch.batch;

import com.example.springbatch.domain.Dept;
import com.example.springbatch.domain.Dept2;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;

/**
 * db to db
 * chunk oriented
 * */
@Slf4j
@RequiredArgsConstructor
@Configuration
public class JpaPageJob2 {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory entityManagerFactory;

    private int chunkSize = 10;

    @Bean
    public Job JpaPageJob2_batchBuild() {
        return jobBuilderFactory.get("jpaPageJob2")
                .start(JpaPageJob2_step1())
                .build();
    }

    @Bean
    public Step JpaPageJob2_step1() {
        return stepBuilderFactory.get("JpaPageJob2_step1")
                .<Dept, Dept2> chunk(chunkSize)
                .reader(jpaPageJob2_dbItemReader())
                .processor(jpaPageJob2_processor())
                .writer(jpaPageJob2_dbItemWriter())
                .build();
    }

    @Bean
    public ItemProcessor<Dept, Dept2> jpaPageJob2_processor() {
        return item -> {
            return new Dept2(item.getDepNo(),"NEW_"+item.getDName(), "NEW_"+item.getLoc());
        };
    }


    @Bean(destroyMethod="")
    public JpaPagingItemReader<Dept> jpaPageJob2_dbItemReader() {
        return new JpaPagingItemReaderBuilder<Dept>()
                .name("jpaPageJob2_dbItemReader")
                .entityManagerFactory(entityManagerFactory)
                .pageSize(chunkSize)
                .queryString("SELECT d from Dept d order by dep_no asc")
                .build();
    }

    @Bean
    public ItemWriter<Dept2> jpaPageJob2_dbItemWriter() {
        JpaItemWriter<Dept2> jpaItemWriter = new JpaItemWriter<>();
        jpaItemWriter.setEntityManagerFactory(entityManagerFactory);
        return jpaItemWriter;
    }
}
