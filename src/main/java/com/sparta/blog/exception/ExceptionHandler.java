package com.sparta.blog.exception;

import org.springframework.web.bind.annotation.RestControllerAdvice;

// 에러처리 더 공부해야돼
@RestControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler
    public String handleException(IllegalArgumentException e) {
        return e.getMessage();
    }
}
