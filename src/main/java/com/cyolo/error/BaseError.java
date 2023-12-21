package com.cyolo.error;

import io.micronaut.http.HttpStatus;

public interface BaseError {
    String getCode();

    HttpStatus getHttpStatus();

    String getDescription();
}
