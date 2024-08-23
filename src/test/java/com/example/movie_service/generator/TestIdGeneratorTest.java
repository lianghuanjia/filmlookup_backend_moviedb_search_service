package com.example.movie_service.generator;

import com.example.movie_service.annotation.TestIdGeneratorAnnotation;
import com.example.movie_service.entity.EntityOne;
import com.example.movie_service.entity.TestEntity;
import com.example.movie_service.junitExtension.MySQLTestContainerExtension;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Id;
import jakarta.persistence.PersistenceContext;

import lombok.Data;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("test") // Explicitly specify to use the configuration in the application-test.properties
@ExtendWith(MySQLTestContainerExtension.class)
@DirtiesContext
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TestIdGeneratorTest {

    @PersistenceContext
    private EntityManager entityManager;

    // @Transactional annotation in tests will automatically roll back the transaction at the end of the test method.
    // This means that any data persisted during the test will not be committed to the database and will be discarded
    // when the test finishes.
    @Transactional
    @Test
    void testTheNewIdWillBeOneWhenNoData() {
        TestEntity testEntity = new TestEntity();
        testEntity.setName("Test1");
        entityManager.persist(testEntity);
        String id = testEntity.getId();
        assertTrue(id.startsWith("ti"));
        String numericPart = id.substring(2);
        assertDoesNotThrow(()->Integer.parseInt(numericPart));
        Integer number = Integer.parseInt(numericPart);
        assertEquals(1, number);
    }

    @Transactional
    @Test
    void testTheNewIdWillBeTenWhenLargestNumericIsNine() {
        TestEntity testEntity1 = new TestEntity();
        testEntity1.setName("Test1");
        entityManager.persist(testEntity1);
        assertTrue(testEntity1.getId().startsWith("ti"));
        System.out.println(testEntity1.getId());

        TestEntity testEntity2 = new TestEntity();
        testEntity2.setName("Test2");
        entityManager.persist(testEntity2);
        assertTrue(testEntity2.getId().startsWith("ti"));
        System.out.println(testEntity2.getId());

        TestEntity testEntity3 = new TestEntity();
        testEntity2.setName("Test3");
        entityManager.persist(testEntity3);
        assertTrue(testEntity3.getId().startsWith("ti"));
        System.out.println(testEntity3.getId());

        String id3 = testEntity3.getId();
        String numericPart = id3.substring(2);
        assertDoesNotThrow(()->Integer.parseInt(numericPart));
        Integer number = Integer.parseInt(numericPart);
        assertEquals(3, number);
    }

    @Transactional
    @Test
    void testDiffEntity() {
        EntityOne entityOne = new EntityOne();
        entityManager.persist(entityOne);
        assertTrue(entityOne.getDfId().startsWith("DE"));
        assertEquals("DE1", entityOne.getDfId());
    }
}
