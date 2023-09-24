package com.nfa.config;


import com.nfa.batch.*;
import com.nfa.client.BBCClient;
import com.nfa.client.GnewsClient;
import com.nfa.client.responses.BBCArticle;
import com.nfa.client.responses.GnewsArticle;
import com.nfa.entities.Article;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Configuration
public class BatchConfig {


    @Autowired
    BBCProcessor bbcProcessor;

    @Autowired
    GnewsClient gnewsClient;

    @Autowired
    BBCClient bbcClient;

    @Bean
    public Job newsFetcherJob(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new JobBuilder("newsFetcherJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(gnewsStep(jobRepository, transactionManager))
                .next(bbcStep(jobRepository, transactionManager))
                .build();
    }

    @Bean

    public Step gnewsStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("Gnews Step", jobRepository)
                .<GnewsArticle, Article>chunk(20, transactionManager)
                .reader(gnewsReader())
                .processor(gnewsProcessor())
                .writer(writer())
                .build();
    }

    @Bean
    @StepScope
    public ItemReader<GnewsArticle> gnewsReader() {
        return new RestApiItemReader(gnewsClient, GnewsArticle.class);
    }

    @Bean
    @StepScope
    public ItemProcessor<GnewsArticle, Article> gnewsProcessor() {
        return new GnewsProcessor();
    }

    @StepScope
    @Bean
    public ItemWriter<Article> writer() {
        return new ArticleWriter();
    }

   

    public Step bbcStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("BBC Step", jobRepository)
                .<BBCArticle, Article>chunk(20, transactionManager)
                .reader(bbcReader())
                .processor(bbcProcessor())
                .writer(writer())
                .build();
    }

    @Bean
    @StepScope
    public ItemReader<BBCArticle> bbcReader() {
        return new RestApiItemReader(bbcClient, BBCArticle.class);
    }

    @Bean
    @StepScope
    public ItemProcessor<BBCArticle, Article> bbcProcessor() {
        return new BBCProcessor();
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        return new JpaTransactionManager();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
