package com.example.movie_service.moviesearch.unit.entity;

import com.example.movie_service.entity.mappingholder.MappingHolder;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MappingHolderUnitTests {
    @Test
    void testPrivateFieldId() throws NoSuchFieldException, IllegalAccessException {
        // Create an instance of MappingHolder
        MappingHolder mappingHolder = new MappingHolder();

        // Use reflection to access the private field "id"
        Field idField = MappingHolder.class.getDeclaredField("id");
        idField.setAccessible(true);

        // Set a value for the private field "id"
        int expectedId = 42;
        idField.set(mappingHolder, expectedId);

        // Retrieve the value of the private field "id"
        int actualId = (int) idField.get(mappingHolder);

        // Assert that the value of the private field is as expected
        assertEquals(expectedId, actualId);
    }
}
