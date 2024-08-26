package com.example.movie_service.integrationTest.testContainer;

import com.example.movie_service.integrationTest.util.junitExtension.MySQLTestContainerExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MySQLTestContainerExtension.class)
@DirtiesContext
@SpringBootTest
class MySQLTestContainerConnectionIntegrationTests {

    @Autowired
    private DataSource dataSource;

    @Test
    void testDatabaseConnection() throws SQLException {
        // Get a connection from the DataSource
        try (Connection connection = dataSource.getConnection()) {
            // Log the JDBC URL to ensure it's pointing to the Testcontainers instance
            String jdbcUrl = connection.getMetaData().getURL();
            System.out.println("Connected to database: " + jdbcUrl);

            // Assert that the connection is not null
            assertNotNull(connection);
        }
    }
}
