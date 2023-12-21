package com.cyolo.error;

import static io.micronaut.http.HttpStatus.INTERNAL_SERVER_ERROR;

import io.micronaut.http.HttpStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CommonError implements BaseError {
    SERVICE_ERROR("BAF-COMMON-100", INTERNAL_SERVER_ERROR, "Internal service error");

    private final String code;
    private final HttpStatus httpStatus;
    private final String description;
}