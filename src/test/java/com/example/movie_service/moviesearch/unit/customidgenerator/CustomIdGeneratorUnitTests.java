package com.example.movie_service.moviesearch.unit.customidgenerator;

import com.example.movie_service.annotation.CustomIdGeneratorAnnotation;
import com.example.movie_service.exception.NoCustomIdGeneratorAnnotationFoundInEntityException;
import com.example.movie_service.generator.CustomIdGenerator;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.generator.EventType;
import org.hibernate.query.spi.QueryImplementor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CustomIdGeneratorUnitTests {

    private CustomIdGenerator customIdGenerator;

    @BeforeEach
    void setUp() {
        customIdGenerator = new CustomIdGenerator();
    }

    @Test
    void shouldThrowNoCustomIdGeneratorAnnotationFoundInEntityExceptionWhenAnnotationIsNull() {
        // Arrange
        SharedSessionContractImplementor session = mock(SharedSessionContractImplementor.class);
        EntityWithoutCustomIdGeneratorAnnotation entity = new EntityWithoutCustomIdGeneratorAnnotation();

        // Act & Assert
        assertThrows(NoCustomIdGeneratorAnnotationFoundInEntityException.class, () ->
                customIdGenerator.generate(session, entity, null, EventType.INSERT));
    }

    @Test
    void shouldGenerateCorrectIdBasedOnMaxNumericPartAndPrefix() {
        // Arrange
        SharedSessionContractImplementor session = mock(SharedSessionContractImplementor.class);
        TestEntityWithAnnotation entity = new TestEntityWithAnnotation();

        // Mocking the behavior of getMaxIdNumericPartForEntity
        CustomIdGenerator spyCustomIdGenerator = Mockito.spy(customIdGenerator);

        // Mock the return value for the method that queries the max numeric part
        // Use "doReturn(...).when(...)" instead of "when(...).thenReturn(...)" when we don't want the method
        // inside when part actually executed
        doReturn(100).when(spyCustomIdGenerator).getMaxIdNumericPartForEntity(session, TestEntityWithAnnotation.class, "id");
        // Act
        Object generatedId = spyCustomIdGenerator.generate(session, entity, null, EventType.INSERT);

        // Assert
        assertEquals("PR101", generatedId);
    }

    @SuppressWarnings({"unchecked"})
    @Test
    void shouldReturnMaxIdNumericPartWhenResultIsNotNull() {
        // Set up
        String idProperty = "id";

        // Mock the session
        SharedSessionContractImplementor session = mock(SharedSessionContractImplementor.class);
        // Mock the Hibernate-specific QueryImplementor
        // In the test scenario, we had to use QueryImplementor because Hibernate 6.x returns this specific
        // implementation of the TypedQuery interface. When mocking the return type of createQuery,
        // using QueryImplementor ensures compatibility with internal expectations of Hibernate.
        QueryImplementor<Integer> query = mock(QueryImplementor.class);

        // Mock the session.createQuery method to return the QueryImplementor
        when(session.createQuery(anyString(), eq(Integer.class))).thenReturn(query);

        // Mock the query.getSingleResult method to return a valid result
        when(query.getSingleResult()).thenReturn(100);

        // Act
        int result = customIdGenerator.getMaxIdNumericPartForEntity(session, TestEntityWithAnnotation.class, idProperty);

        // Assert
        assertEquals(100, result);
    }

    @SuppressWarnings({"unchecked"})
    @Test
    void shouldReturnZeroWhenResultIsNull() {
        // Arrange
        SharedSessionContractImplementor session = mock(SharedSessionContractImplementor.class);
        QueryImplementor<Integer> query = mock(QueryImplementor.class);

        // Mocking session.createQuery to return the mock query
        when(session.createQuery(anyString(), eq(Integer.class))).thenReturn(query);

        // Mocking query.getSingleResult to return null
        when(query.getSingleResult()).thenReturn(null);

        // Act
        int result = customIdGenerator.getMaxIdNumericPartForEntity(session, TestEntityWithAnnotation.class, "id");

        // Assert
        assertEquals(0, result);
    }

    @SuppressWarnings({"unchecked"})
    @Test
    void shouldThrowExceptionWhenQueryExecutionFails() {
        // Arrange
        SharedSessionContractImplementor session = mock(SharedSessionContractImplementor.class);
        QueryImplementor<Integer> query = mock(QueryImplementor.class);

        // Mocking session.createQuery to return the mock query
        when(session.createQuery(anyString(), eq(Integer.class))).thenReturn(query);

        // Mocking query.getSingleResult to throw an exception
        when(query.getSingleResult()).thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> customIdGenerator.getMaxIdNumericPartForEntity(session, TestEntityWithAnnotation.class, "id"));
    }


    // Test entity class with the annotation
    @SuppressWarnings({"unused"})
    @Entity
    private static class TestEntityWithAnnotation {
        @Id
        @CustomIdGeneratorAnnotation(prefix = "PR")
        private String id;
    }

    @SuppressWarnings({"unused"})
    // Test class without the annotation
    private static class EntityWithoutCustomIdGeneratorAnnotation {
        private String id;
    }
}
