package com.example.movie_service.generator;

import com.example.movie_service.annotation.CustomIdGeneratorAnnotation;
import com.example.movie_service.exception.NoCustomIdGeneratorAnnotationFoundInEntityException;
import jakarta.persistence.TypedQuery;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.generator.BeforeExecutionGenerator;
import org.hibernate.generator.EventType;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.EnumSet;

@Slf4j
@Component
public class CustomIdGenerator implements BeforeExecutionGenerator {


    @Override
    public Object generate(SharedSessionContractImplementor sharedSessionContractImplementor, Object entity, Object o1,
                           EventType eventType) {

        Class<?> entityClass = entity.getClass();

        // Find the ID field that has the annotation
        Field idField = findIdField(entityClass);
        CustomIdGeneratorAnnotation annotation = idField.getAnnotation(CustomIdGeneratorAnnotation.class);

        String idProperty = idField.getName();
        String prefix = annotation.prefix();

        int maxNumericPart = getMaxIdNumericPartForEntity(sharedSessionContractImplementor, entityClass, idProperty);
        int nextIdNumber = maxNumericPart+1;
        return prefix + nextIdNumber;
    }

    @Override
    public EnumSet<EventType> getEventTypes() {
        return EnumSet.of(EventType.INSERT);
    }


    public <T> int getMaxIdNumericPartForEntity(SharedSessionContractImplementor session, Class<T> entityClass, String idProperty) {
        // Construct the JPQL query
        String queryString = "SELECT MAX(CAST(SUBSTRING(e." + idProperty + ", 3) AS integer)) FROM " + entityClass.getSimpleName() + " e";

        // Create a TypedQuery for the entity class
        TypedQuery<Integer> query = session.createQuery(queryString, Integer.class);

        try {
            // Execute the query and get the result
            Integer result = query.getSingleResult();

            // If the result is null (no records), return 0
            return result != null ? result : 0;
        } catch (Exception e) {
            log.error("Error executing query to get max ID numeric part for entity: {}", entityClass.getSimpleName(), e);
            // Return a default value of 0 in case of an error
            throw e;
        }
    }

    private Field findIdField(Class<?> clazz) {
        // Iterate over the fields to find the one annotated with the annotation that represents custom id generation
        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(CustomIdGeneratorAnnotation.class)) {
                return field;
            }
        }
        throw new NoCustomIdGeneratorAnnotationFoundInEntityException("Entity class " + clazz.getSimpleName() + " does not have @" + CustomIdGeneratorAnnotation.class.getSimpleName());
    }
}
