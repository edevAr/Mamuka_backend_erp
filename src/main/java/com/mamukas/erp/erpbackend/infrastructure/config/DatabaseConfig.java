package com.mamukas.erp.erpbackend.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Database configuration that handles both standard JDBC URLs and Render's DATABASE_URL format
 * Render provides DATABASE_URL in format: postgresql://user:password@host:port/database
 * Spring Boot expects: jdbc:postgresql://host:port/database
 */
@Configuration
public class DatabaseConfig {

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
        DataSourceBuilder<?> builder = DataSourceBuilder.create();
        
        // If DATABASE_URL is provided (Render format), parse it
        if (databaseUrl != null && !databaseUrl.isEmpty() && !databaseUrl.startsWith("jdbc:")) {
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
                
                return builder.build();
            } catch (URISyntaxException e) {
                throw new RuntimeException("Invalid DATABASE_URL format: " + databaseUrl, e);
            }
        }
        
        // Otherwise, use standard Spring configuration from application.properties
        builder.url(springDatasourceUrl);
        builder.username(springDatasourceUsername);
        builder.password(springDatasourcePassword);
        builder.driverClassName("org.postgresql.Driver");
        
        return builder.build();
    }
}

