package com.nfa.config;


import com.nfa.batch.processors.BBCProcessor;
import com.nfa.batch.processors.GnewsProcessor;
import com.nfa.batch.processors.NYTProcessor;
import com.nfa.batch.readers.ArticleFromDbItemReader;
import com.nfa.batch.readers.KeywordMapper;
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
import com.nfa.entity.Article;
import com.nfa.repository.ArticleRepository;
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
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Configuration
public class BatchConfig {

    @Autowired
    GnewsClient gnewsClient;

    @Autowired
    BBCClient bbcClient;

    @Autowired
    NYTClient nytClient;

    @Autowired
    ArticleRepository articleRepository;

    @Autowired
    ToKafkaWriter toKafkaWriter;

    @Value("classpath:${file.path.keywords}")
    Resource resource;

    @Bean
    public Job newsFetcherJob(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new JobBuilder("newsFetcherJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(updateKeywordsStep(jobRepository, transactionManager))
                .next(gnewsStep(jobRepository, transactionManager))
                .next(bbcStep(jobRepository, transactionManager))
                .next(nytStep(jobRepository, transactionManager))
                .next(produceToKafkaStep(jobRepository, transactionManager))
                .build();
    }

    @Bean
    public Step updateKeywordsStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("Keywords Step", jobRepository)
                .<KeywordDto, KeywordDto>chunk(20, transactionManager)
                .reader(keywordsReader())
                .writer(keywordsWriter())
                .build();
    }

    @Bean
    @StepScope
    public FlatFileItemReader<KeywordDto> keywordsReader() {
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setDelimiter("|");
        tokenizer.setNames("name", "rule");
        DefaultLineMapper<KeywordDto> keywordLineMapper = new DefaultLineMapper<>();
        keywordLineMapper.setLineTokenizer(tokenizer);
        keywordLineMapper.setFieldSetMapper(new KeywordMapper());
        keywordLineMapper.afterPropertiesSet();
        FlatFileItemReader<KeywordDto> reader = new FlatFileItemReader<>();
        reader.setLineMapper(keywordLineMapper);
        reader.setResource(resource);
        return reader;
    }

    @Bean
    @StepScope
    public ItemWriter<KeywordDto> keywordsWriter() {
        return new KeywordWriter();
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

    @Bean
    public PlatformTransactionManager transactionManager() {
        return new JpaTransactionManager();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
