package com.example.movie_service.generator;

import com.example.movie_service.entity.TestEntity;
import com.example.movie_service.junitExtension.MySQLTestContainerExtension;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    @Test
    void testCustomIdGeneration() {
        TestEntity testEntity = new TestEntity();
        testEntity.setName("test");

        entityManager.persist(testEntity);

        String id = testEntity.getId();
        assertNotNull(id);
        assertTrue(id.startsWith("tt"));
        assertEquals(9, id.length());
        System.out.println(id);
    }
}
