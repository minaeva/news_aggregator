package com.nfa.config;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "secondaryEntityManagerFactory",
        transactionManagerRef = "secondaryTransactionManager",
        basePackages = "com.nfa.repository.secondary")
public class SecondaryDBConfig {

    @Bean(name = "secondaryDataSource")
    @ConfigurationProperties(prefix = "spring.secondary-datasource")
    public DataSource secondDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "secondaryEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean secondEntityManagerFactory(
            EntityManagerFactoryBuilder builder, @Qualifier("secondaryDataSource") DataSource dataSource) {
        return builder
                .dataSource(dataSource)
                .packages("com.nfa.entity.secondary")
                .persistenceUnit("secondary")
                .build();
    }

    @Bean(name = "secondaryTransactionManager")
    public PlatformTransactionManager secondaryTransactionManager(
            @Qualifier("secondaryEntityManagerFactory") EntityManagerFactory secondaryEntityManagerFactory) {
        return new JpaTransactionManager(secondaryEntityManagerFactory);
    }

}
