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

/**
 *  The numeric value will be the maximum numeric part in the column of a table.
 *        For example: in a table called movie, and there are 5 movies in the table. Their primary key is movie_id, with
 *        5 of them being: tt001, tt002, tt003, tt050, tt100. The prefix is tt. The strategy of this custom id generator
 *        will look for the biggest numeric part of those id, in this case, 100, and plus 1. So the new id will be
 *  This CustomIdGenerator is mainly used for Movie and Person entities' ids.
 *        Movie entity's primary key: tt + numeric. E.g.: tt001
 *        Person entity's primary key: nm + numeric. E.g.: nm001
 */

@Slf4j
@Component
public class CustomIdGenerator implements BeforeExecutionGenerator {

    /**
     * Generate a custom id, which contains a custom prefix and a numeric part.
     * @param sharedSessionContractImplementor This parameter represents the session or context in which the entity is
     *                                        being persisted.You can use this session to query the database or perform
     *                                        other operations related to ID generation.
     *
     * @param entity This parameter is the entity object for which the ID is being generated. The entity contains the
     *              data that will be persisted to the database. By accessing the entity, you can extract information
     *              needed for ID generation, such as field values or annotations.
     *
     * @param o1 This parameter is reserved for future use in Hibernate and is not commonly utilized in ID generation.
     *           In most cases, you can ignore this parameter. It is present in the method signature for potential
     *           extension or future functionality.
     *
     * @param eventType indicates the type of event that is triggering the ID generation. The EventType enum represents
     *                  various types of persistence-related events in Hibernate, such as INSERT, UPDATE, DELETE, etc.
     *                  In the context of BeforeExecutionGenerator, the event type is usually INSERT, indicating that
     *                  a new entity is being persisted.
     * @return A String custom id.
     */
    @Override
    public Object generate(SharedSessionContractImplementor sharedSessionContractImplementor, Object entity, Object o1,
                           EventType eventType) {

        Class<?> entityClass = entity.getClass();

        // Find the ID field that has the annotation
        Field idField = findIdField(entityClass);
        CustomIdGeneratorAnnotation annotation = idField.getAnnotation(CustomIdGeneratorAnnotation.class);

        // Get the field's name annotated with CustomIdGeneratorAnnotation
        String idProperty = idField.getName();

        // Get the prefix from the CustomIdGeneratorAnnotation annotation
        String prefix = annotation.prefix();

        int maxNumericPart = getMaxIdNumericPartForEntity(sharedSessionContractImplementor, entityClass, idProperty);
        int nextIdNumber = maxNumericPart+1;
        return prefix + nextIdNumber;
    }

    /**
     * It specifies the types of events that the ID generator should respond to. By overriding this method, you define
     * which persistence events (e.g., INSERT, UPDATE, DELETE) will trigger the ID generation logic.
     *
     * @return EnumSet.of(EventType.INSERT)
     *          By returning EnumSet.of(EventType.INSERT), you're specifying that the ID generator should only respond
     *          to INSERT events, meaning the ID generation logic will only be triggered when a new entity is being
     *          persisted (inserted) into the database.
     */
    @Override
    public EnumSet<EventType> getEventTypes() {
        return EnumSet.of(EventType.INSERT);
    }

    /**
     * It gets the maximum numeric part of an id that represents an Entity's primary key. For example, if there are 3
     * Movie entities with ids being tt001, tt005, tt100, this method will return 100.
     * WARNING: This method assumes that the primary key is composed of a 2-character prefix and a numeric part.
     * @param session represents the session or context in which the entity is
     *                being persisted.You can use this session to query the database or perform
     *                other operations related to ID generation.
     * @param entityClass The entity class that generates the custom id
     * @param idProperty The field name annotated with @id in the entity
     * @return an int that is the max numeric part of all those ids in the entityClass's table
     * @param <T> T is a generic type parameter that represents the entity class
     */
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

    /**
     * This method is used to find the field in a given entity class that is marked with @CustomIdGeneratorAnnotation.
     * This is crucial for identifying which field in the entity requires custom ID generation.
     * @param clazz The class object representing the entity class that you are inspecting.
     * @return This is the field that should use the custom ID generation strategy.
     *          If a field with the annotation is found, the method returns that field.
     */
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
