package com.cyolo.error;

import static io.micronaut.http.HttpStatus.BAD_REQUEST;
import static io.micronaut.http.HttpStatus.NOT_FOUND;

import io.micronaut.http.HttpStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum WordsFrequencyEvaluatorError implements BaseError {
    WORDS_NOT_FOUND("WFE-001", NOT_FOUND, "Words not found"),
    NO_WORDS_PROVIDED("WFE-002", BAD_REQUEST, "No words provided by request");

    private final String code;
    private final HttpStatus httpStatus;
    private final String description;
}
