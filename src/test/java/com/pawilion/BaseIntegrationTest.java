package com.pawilion;

import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import java.util.List;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.testcontainers.containers.PostgreSQLContainer;

public abstract class BaseIntegrationTest {

  static PostgreSQLContainer<?> postgresqlContainer;

  @BeforeAll
  static void startContainer() {
    postgresqlContainer =
        new PostgreSQLContainer<>("postgres:13-alpine")
            .withDatabaseName("pet_shop")
            .withUsername("postgres")
            .withPassword("postgres")
            .withExposedPorts(5438)
            .withCreateContainerCmdModifier(
                cmd ->
                    cmd.withCmd(
                            List.of(
                                "-p 5438",
                                "-c",
                                "fsync=off",
                                "-c",
                                "shared_buffers=512MB",
                                "-c",
                                "max_connections=100"))
                        .withHostConfig(
                            new HostConfig()
                                .withPortBindings(
                                    new PortBinding(
                                        Ports.Binding.bindPort(5438), new ExposedPort(5438)))));
    postgresqlContainer.start();
  }

  @AfterAll
  static void stopContainer() {
    postgresqlContainer.stop();
  }
}
