package com.pawilion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.r2dbc.R2dbcAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.web.reactive.config.EnableWebFlux;

@SpringBootApplication(exclude = {R2dbcAutoConfiguration.class, DataSourceAutoConfiguration.class})
@EnableWebFlux
@EnableR2dbcAuditing
@EnableJpaRepositories
@EnableR2dbcRepositories
@EnableConfigurationProperties
public class PawilionApplication {

  public static void main(String[] args) {
    SpringApplication.run(PawilionApplication.class, args);
  }
}
