package com.example.movie_service.junitExtension;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;

import static com.example.movie_service.constants.TestConstant.SQL_VERSION;

public class MySQLTestContainerExtension implements BeforeAllCallback, AfterAllCallback {

    private MySQLContainer<?> mysqlContainer;
//    private static final MySQLContainer<?> mysqlContainer = new MySQLContainer<>(SQL_VERSION)
//            .withDatabaseName("testDB")
//            .withUsername("testUser")
//            .withPassword("testPassword")
//            .withReuse(true);

    @Override
    public void beforeAll(ExtensionContext context) {
        mysqlContainer = new MySQLContainer<>(SQL_VERSION)
                .withDatabaseName("testDB")
                .withUsername("testUser")
                .withPassword("testPassword")
                .withReuse(true);

        mysqlContainer.start();

        // Set properties to be used by Spring's datasource
        System.setProperty("spring.datasource.url", mysqlContainer.getJdbcUrl());
        System.setProperty("spring.datasource.username", mysqlContainer.getUsername());
        System.setProperty("spring.datasource.password", mysqlContainer.getPassword());
        System.setProperty("spring.datasource.driver-class-name", mysqlContainer.getDriverClassName());
    }

    @Override
    public void afterAll(ExtensionContext extensionContext) throws Exception {
        // Do nothing, Testcontainers handles container shutdown
    }

//    @DynamicPropertySource
//    static void setUpProperties(DynamicPropertyRegistry registry) {
//        registry.add("spring.datasource.url", mysqlContainer::getJdbcUrl);
//        registry.add("spring.datasource.username", mysqlContainer::getUsername);
//        registry.add("spring.datasource.password", mysqlContainer::getPassword);
//        registry.add("spring.datasource.driver-class-name", mysqlContainer::getDriverClassName);
//    }
}
