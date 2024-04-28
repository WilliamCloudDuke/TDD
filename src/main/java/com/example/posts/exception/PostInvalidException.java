package com.example.posts.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PostInvalidException extends RuntimeException {

    public PostInvalidException(String message) {
        super(message);
    }
}
