package com.cyolo.error;

import static io.micronaut.http.HttpStatus.BAD_REQUEST;
import static io.micronaut.http.HttpStatus.NOT_FOUND;

import io.micronaut.http.HttpStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum WordsFrequencyEvaluatorException implements BaseError {
    WORDS_NOT_FOUND("WFE-001", NOT_FOUND, "Words not found"),
    INVALID_WORDS_FORMAT("WFE-002", BAD_REQUEST, "Input words have invalid format");

    private final String code;
    private final HttpStatus httpStatus;
    private final String description;
}
