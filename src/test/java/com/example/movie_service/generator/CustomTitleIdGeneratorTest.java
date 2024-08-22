package com.example.movie_service.generator;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.Serializable;
import java.util.Properties;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class CustomTitleIdGeneratorTest {

    @InjectMocks
    private CustomTitleIdGenerator customTitleIdGenerator;

    @Before
    public void setUp() {
        // Configure the generator with different prefixes
        Properties properties = new Properties();
        properties.setProperty("prefix", "tt");
        customTitleIdGenerator.configure(null, properties, null);
    }

    @Test
    public void testGenerateWithTTPrefix() {
        SharedSessionContractImplementor session = mock(SharedSessionContractImplementor.class);
        Object obj = new Object();

        Serializable generatedId = customTitleIdGenerator.generate(session, obj);
        assertTrue(generatedId.toString().startsWith("tt"));
    }

    @Test
    public void testGenerateWithNMPrefix() {
        Properties properties = new Properties();
        properties.setProperty("prefix", "nm");
        customTitleIdGenerator.configure(null, properties, null);

        SharedSessionContractImplementor session = mock(SharedSessionContractImplementor.class);
        Object obj = new Object();

        Serializable generatedId = customTitleIdGenerator.generate(session, obj);
        assertTrue(generatedId.toString().startsWith("nm"));
    }
}

