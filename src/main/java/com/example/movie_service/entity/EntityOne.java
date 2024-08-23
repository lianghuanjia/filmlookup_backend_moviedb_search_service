package com.example.movie_service.entity;

import com.example.movie_service.annotation.TestIdGeneratorAnnotation;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class EntityOne {
    @Id
    @TestIdGeneratorAnnotation(entityClass = EntityOne.class, prefix = "DE")
    private String dfId;
}
