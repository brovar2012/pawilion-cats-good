package com.pawilion.config.db;

import io.r2dbc.postgresql.PostgresqlConnectionConfiguration;
import io.r2dbc.postgresql.PostgresqlConnectionFactory;
import io.r2dbc.spi.ConnectionFactory;
import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
import org.springframework.r2dbc.core.DatabaseClient;

@Configuration
public class DataSourceConfig {

  @Value("${spring.datasource.r2dbc.username}")
  private String readerUsername;

  @Value("${spring.datasource.r2dbc.password}")
  private String readerPassword;

  @Value("${spring.datasource.r2dbc.host}")
  private String readerHost;

  @Value("${spring.datasource.r2dbc.port}")
  private Integer readerPort;

  @Value("${spring.datasource.r2dbc.db-name}")
  private String readerDbName;

  @Bean
  public ConnectionFactory connectionFactory() {
    return
            new PostgresqlConnectionFactory(
                    PostgresqlConnectionConfiguration.builder()
                            .database(readerDbName)
                            .host(readerHost)
                            .port(readerPort)
                            .username(readerUsername)
                            .password(readerPassword)
                            .build());
  }

  @Bean
  public DatabaseClient databaseClient(ConnectionFactory connectionFactory) {
    return DatabaseClient.create(connectionFactory);
  }

}
