package com.example.movie_service.annotation;

import com.example.movie_service.generator.TestIdGenerator;
import org.hibernate.annotations.IdGeneratorType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@IdGeneratorType(TestIdGenerator.class)
public @interface TestIdGeneratorAnnotation {
    Class<?> entityClass();
    String prefix();
}
