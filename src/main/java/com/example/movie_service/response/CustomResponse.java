package com.example.movie_service.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor //If I use this annotation, when I call the AllArgs Constructor, how do I call it
@NoArgsConstructor
public class CustomResponse<T> {
    private int code;
    private String message;
    private T data;
}
