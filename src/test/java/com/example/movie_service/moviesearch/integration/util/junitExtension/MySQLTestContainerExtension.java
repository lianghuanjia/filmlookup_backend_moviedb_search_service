package com.example.movie_service.moviesearch.integration.util.junitExtension;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.testcontainers.containers.MySQLContainer;

import static com.example.movie_service.constants.TestConstant.SQL_VERSION;

public class MySQLTestContainerExtension implements BeforeAllCallback {
    private static final String testDatabaseName = "testDB";
    private static final String testUsername = "testUser";
    private static final String testPassword = "testPassword";


    @SuppressWarnings({"resource"})
    // JUnit 5 will shut down the container after the test, so we don't need to close the MySQLContainer manually
    @Override
    public void beforeAll(ExtensionContext context) {
        MySQLContainer<?> mysqlContainer = new MySQLContainer<>(SQL_VERSION)
                .withDatabaseName(testDatabaseName)
                .withUsername(testUsername)
                .withPassword(testPassword);

        mysqlContainer.start();

        // Set properties to be used by Spring's datasource
        System.setProperty("spring.datasource.url", mysqlContainer.getJdbcUrl());
        System.setProperty("spring.datasource.username", mysqlContainer.getUsername());
        System.setProperty("spring.datasource.password", mysqlContainer.getPassword());
        System.setProperty("spring.datasource.driver-class-name", mysqlContainer.getDriverClassName());
    }

}
