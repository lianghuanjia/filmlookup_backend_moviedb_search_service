package com.example.movie_service.generator;

import com.example.movie_service.dataInitService.DataInitializerService;
import com.example.movie_service.entity.Movie;
import com.example.movie_service.entity.TestEntity;
import com.example.movie_service.junitExtension.MySQLTestContainerExtension;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static com.example.movie_service.constants.TestConstant.MOVIE_1_TITLE;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("test") // Explicitly specify to use the configuration in the application-test.properties
@ExtendWith(MySQLTestContainerExtension.class)
@DirtiesContext
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TestIdGeneratorTest {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private DataInitializerService dataInitializerService;

    @AfterEach
    void afterEach() {
        dataInitializerService.clearDatabase();
    }

    @Transactional
    @Test
    void testTheNewIdWillBeOneWhenNoData() {
        TestEntity testEntity = new TestEntity();
        testEntity.setName("Test1");
        entityManager.persist(testEntity);
        String id = testEntity.getId();
        String numericPart = id.substring(2);
        assertDoesNotThrow(()->Integer.parseInt(numericPart));
        Integer number = Integer.parseInt(numericPart);
        assertEquals(1, number);
        System.out.println(id);
    }
}
