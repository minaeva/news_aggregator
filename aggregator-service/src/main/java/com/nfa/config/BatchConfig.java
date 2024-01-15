package com.nfa.config;


import com.nfa.batch.processors.BBCProcessor;
import com.nfa.batch.processors.GnewsProcessor;
import com.nfa.batch.processors.KeywordProcessor;
import com.nfa.batch.processors.NYTProcessor;
import com.nfa.batch.readers.ArticleFromDbItemReader;
import com.nfa.batch.readers.KeywordReader;
import com.nfa.batch.readers.RestApiItemReader;
import com.nfa.batch.writers.ArticleWriter;
import com.nfa.batch.writers.KeywordWriter;
import com.nfa.batch.writers.ToKafkaWriter;
import com.nfa.client.BBCClient;
import com.nfa.client.GnewsClient;
import com.nfa.client.NYTClient;
import com.nfa.client.responses.BBCArticle;
import com.nfa.client.responses.GnewsArticle;
import com.nfa.client.responses.NYTArticle;
import com.nfa.dto.KeywordDto;
import com.nfa.entity.primary.Article;
import com.nfa.entity.secondary.SecondaryKeyword;
import com.nfa.repository.primary.ArticleRepository;
import lombok.AllArgsConstructor;
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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@Configuration
@AllArgsConstructor
public class BatchConfig {

    private final KeywordReader keywordReader;
    private final KeywordProcessor keywordProcessor;
    private final KeywordWriter keywordWriter;
    private final GnewsClient gnewsClient;
    private final BBCClient bbcClient;
    private final NYTClient nytClient;
    private final ArticleRepository articleRepository;
    private final ToKafkaWriter toKafkaWriter;

    @Bean
    public Job newsFetcherJob(JobRepository jobRepository,
                              @Qualifier("primaryTransactionManager") PlatformTransactionManager primaryTransactionManager,
                              @Qualifier("secondaryTransactionManager") PlatformTransactionManager secondaryTransactionManager) {
        return new JobBuilder("newsFetcherJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(keywordStep(jobRepository, secondaryTransactionManager))
                .next(gnewsStep(jobRepository, primaryTransactionManager))
                .next(bbcStep(jobRepository, primaryTransactionManager))
                .next(nytStep(jobRepository, primaryTransactionManager))
                .next(produceToKafkaStep(jobRepository, primaryTransactionManager))
                .build();
    }

    @Bean
    public Step keywordStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("Keywords Step", jobRepository)
                .<SecondaryKeyword, KeywordDto>chunk(20, transactionManager)
                .reader(keywordReader)
                .processor(keywordProcessor)
                .writer(keywordWriter)
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

    @Bean
    @StepScope
    public ItemWriter<Article> writer() {
        return new ArticleWriter();
    }

    @Bean
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
    public Step nytStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("NYT Step", jobRepository)
                .<NYTArticle, Article>chunk(20, transactionManager)
                .reader(nytReader())
                .processor(nytProcessor())
                .writer(writer())
                .build();
    }

    @Bean
    @StepScope
    public ItemReader<NYTArticle> nytReader() {
        return new RestApiItemReader(nytClient, NYTArticle.class);
    }

    @Bean
    @StepScope
    public ItemProcessor<NYTArticle, Article> nytProcessor() {
        return new NYTProcessor();
    }

    @Bean
    public Step produceToKafkaStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("To Kafka Step", jobRepository)
                .<Article, Article>chunk(20, transactionManager)
                .reader(toKafkaReader())
                .writer(toKafkaWriter)
                .build();
    }

    @Bean
    @StepScope
    public ItemReader<Article> toKafkaReader() {
        return new ArticleFromDbItemReader(articleRepository);
    }

}
