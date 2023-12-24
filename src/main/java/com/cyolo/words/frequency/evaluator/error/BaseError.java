package com.cyolo.words.frequency.evaluator.error;

import io.micronaut.http.HttpStatus;

public interface BaseError {
    String getCode();

    HttpStatus getHttpStatus();

    String getDescription();
}
