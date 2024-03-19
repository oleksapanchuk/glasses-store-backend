package com.oleksa.ecommerce.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class UnauthorizedAccessException extends RuntimeException {

    public UnauthorizedAccessException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("Unauthorized access to %s with %s : '%s'", resourceName, fieldName, fieldValue));
    }
}