package com.mamukas.erp.erpbackend.infrastructure.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;

/**
 * Database configuration that handles both standard JDBC URLs and Render's DATABASE_URL format
 * Render provides DATABASE_URL in format: postgresql://user:password@host:port/database
 * Spring Boot expects: jdbc:postgresql://host:port/database
 */
@Configuration
public class DatabaseConfig {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseConfig.class);

    @Value("${DATABASE_URL:}")
    private String databaseUrl;

    @Value("${SPRING_DATASOURCE_URL:jdbc:postgresql://localhost:5432/mamukas_erp}")
    private String springDatasourceUrl;

    @Value("${DB_USERNAME:postgres}")
    private String springDatasourceUsername;

    @Value("${DB_PASSWORD:postgres}")
    private String springDatasourcePassword;

    /**
     * Creates a DataSource that can handle both Render's DATABASE_URL and standard Spring configuration
     */
    @Bean
    @Primary
    public DataSource dataSource() {
        logger.info("=== DatabaseConfig: Initializing DataSource ===");
        logger.info("DATABASE_URL environment variable: {}", 
            databaseUrl != null && !databaseUrl.isEmpty() ? "SET (hidden)" : "NOT SET");
        
        DataSourceBuilder<?> builder = DataSourceBuilder.create();
        
        // If DATABASE_URL is provided (Render format), parse it
        if (databaseUrl != null && !databaseUrl.isEmpty() && !databaseUrl.startsWith("jdbc:")) {
            logger.info("Parsing DATABASE_URL from Render format...");
            try {
                URI dbUri = new URI(databaseUrl);
                
                String username = dbUri.getUserInfo().split(":")[0];
                String password = dbUri.getUserInfo().split(":")[1];
                String host = dbUri.getHost();
                int port = dbUri.getPort();
                String path = dbUri.getPath();
                String database = path.startsWith("/") ? path.substring(1) : path;
                
                // Build JDBC URL
                String jdbcUrl = String.format("jdbc:postgresql://%s:%d/%s", host, port, database);
                
                builder.url(jdbcUrl);
                builder.username(username);
                builder.password(password);
                builder.driverClassName("org.postgresql.Driver");
                
                logger.info("Using DATABASE_URL from environment: jdbc:postgresql://{}:{}/{}", host, port, database);
                return builder.build();
            } catch (URISyntaxException e) {
                logger.error("Invalid DATABASE_URL format: {}", databaseUrl, e);
                throw new RuntimeException("Invalid DATABASE_URL format: " + databaseUrl, e);
            } catch (Exception e) {
                logger.error("Error parsing DATABASE_URL: {}", e.getMessage(), e);
                throw e;
            }
        }
        
        // Otherwise, use standard Spring configuration from application.properties
        logger.info("Using fallback configuration from application.properties");
        logger.info("URL: {}", springDatasourceUrl);
        logger.info("Username: {}", springDatasourceUsername);
        
        builder.url(springDatasourceUrl);
        builder.username(springDatasourceUsername);
        builder.password(springDatasourcePassword);
        builder.driverClassName("org.postgresql.Driver");
        
        return builder.build();
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        em.setPackagesToScan("com.mamukas.erp.erpbackend");
        
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        
        Properties properties = new Properties();
        properties.setProperty("hibernate.hbm2ddl.auto", "update");
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        properties.setProperty("hibernate.show_sql", "true");
        properties.setProperty("hibernate.format_sql", "true");
        em.setJpaProperties(properties);
        
        return em;
    }

    @Bean
    public PlatformTransactionManager transactionManager(LocalContainerEntityManagerFactoryBean entityManagerFactory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory.getObject());
        return transactionManager;
    }
}

