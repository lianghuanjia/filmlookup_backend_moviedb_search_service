package com.example.movie_service.generator;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.Configurable;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.Type;

import java.io.Serializable;
import java.util.Properties;
import java.util.UUID;

public class CustomTitleIdGenerator implements IdentifierGenerator, Configurable {

    private String prefix;

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object obj) throws HibernateException {
        String uuid = UUID.randomUUID().toString().replace("-", "").substring(0, 7);
        return prefix + uuid;
    }

    @Override
    public void configure(Type type, Properties properties, ServiceRegistry serviceRegistry) throws HibernateException {
        prefix = properties.getProperty("prefix", "tt"); // Default to "tt" if not provided
    }

}
