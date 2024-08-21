package com.example.movie_service.customTitleIdGeneratorTest;

import com.example.movie_service.generator.CustomTitleIdGenerator;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.Type;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.Serializable;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class CustomTitleIdGeneratorTest {

    private CustomTitleIdGenerator idGenerator;

    @BeforeEach
    void setUp() {
        idGenerator = new CustomTitleIdGenerator();
    }

    @Test
    void testConfigureWithCustomPrefix() {
        Properties properties = new Properties();
        properties.setProperty("prefix", "MOV");

        // Mock the necessary dependencies
        Type type = mock(Type.class);

        // Call the configure method with real Properties object
        idGenerator.configure(type, properties, null);

        // Generate an ID
        SharedSessionContractImplementor session = mock(SharedSessionContractImplementor.class);
        Object object = mock(Object.class);
        Serializable generatedId = idGenerator.generate(session, object);

        // Assert the prefix is correctly applied
        assertTrue(generatedId.toString().startsWith("MOV"));
        assertEquals(10, generatedId.toString().length()); // 3 prefix + 7 UUID characters
    }

    @Test
    void testConfigureWithDefaultPrefix() {
        // Mock the necessary dependencies
        Type type = mock(Type.class);

        // Setup properties without a custom prefix
        Properties properties = new Properties(); // No prefix property

        // Call the configure method
        idGenerator.configure(type, properties, null);

        // Generate an ID
        Serializable generatedId = idGenerator.generate(mock(SharedSessionContractImplementor.class), new Object());

        // Assert the default prefix "tt" is correctly applied
        assertTrue(generatedId.toString().startsWith("tt"));
        assertEquals(9, generatedId.toString().length()); // 2 prefix + 7 UUID characters
    }

    @Test
    void testGenerateId() {
        // Mock the necessary dependencies
        Type typeClass = mock(Type.class);
        Properties properties = new Properties();

        idGenerator.configure(typeClass, new Properties(), null);

        // Generate an ID
        Serializable generatedId = idGenerator.generate(mock(SharedSessionContractImplementor.class), mock(Object.class));

        // Assertions
        assertNotNull(generatedId);
        assertTrue(generatedId instanceof String);

        // Verify that the generated ID has the correct length (prefix + 7 characters)
        assertEquals(9, generatedId.toString().length()); // "tt" + 7 UUID characters
    }
}
