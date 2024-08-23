package com.example.movie_service.generator;

import com.example.movie_service.repository.CustomMovieRepository;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceUnit;
import jakarta.persistence.Query;
import lombok.NoArgsConstructor;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.generator.BeforeExecutionGenerator;
import org.hibernate.generator.EventType;
import org.hibernate.query.NativeQuery;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.EnumSet;


@Component
public class TestIdGenerator implements BeforeExecutionGenerator {


    @Override
    public Object generate(SharedSessionContractImplementor sharedSessionContractImplementor, Object o, Object o1, EventType eventType) {
        int maxNumericPart = getMaxIdNumericPart(sharedSessionContractImplementor, "movie", "movie_id");
        int nextIdNumber = maxNumericPart+1;
        return "tt"+nextIdNumber;
    }

    @Override
    public EnumSet<EventType> getEventTypes() {
        return EnumSet.of(EventType.INSERT);
    }


    private int getMaxIdNumericPart(SharedSessionContractImplementor session, String tableName, String idColumn) {
        String queryString = "SELECT MAX(CAST(SUBSTRING(m." + idColumn + ", 3) AS UNSIGNED)) FROM " + tableName + " m";

        NativeQuery<?> query = session.createNativeQuery(queryString);

        try {
            // Execute the query and get the result
            Object result = query.getSingleResult();

            // Check if the result is not null and is a valid number
            if (result instanceof Number number) {
                return number.intValue();
            } else {
                // If result is null or not a number, return 0 as default
                return 0;
            }
        } catch (Exception e) {
            // Handle any exceptions that occur during the query execution
            e.printStackTrace();
            // Return a default value of 0 in case of an error
            throw e;
        }
    }
}
