package com.example.movie_service.annotation;

import com.example.movie_service.entity.movie.Movie;
import com.example.movie_service.entity.person.Person;
import com.example.movie_service.generator.CustomIdGenerator;
import org.hibernate.annotations.IdGeneratorType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for generating custom IDs using the {@link CustomIdGenerator}.
 * <p>
 * This annotation is used to mark a field as requiring a custom ID generation strategy.
 * The ID will be generated with a specified prefix followed by an auto-incrementing numeric value.
 * </p>
 *
 * <p>
 * WARNING: prefix: It must be length of 2. This annotation is mainly designed to generate Person and Movie's id,
 * because Person's id starts with "nm" + number, and Movie's id starts with "tt" + number.
 * @see Movie ;
 * @see Person ;
 * </p>
 *
 * @see CustomIdGenerator
 */

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@IdGeneratorType(CustomIdGenerator.class)
public @interface CustomIdGeneratorAnnotation {
    // WARNING: prefix's length must be 2
    String prefix();
}
