package com.cyolo.error;

import java.util.UUID;
import java.util.stream.Collectors;

import io.micronaut.context.annotation.Requires;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.http.server.exceptions.response.Error;
import io.micronaut.http.server.exceptions.response.ErrorContext;
import io.micronaut.http.server.exceptions.response.ErrorResponseProcessor;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;

@Singleton
@Requires
@Slf4j
public class BaseErrorResponseProcessor implements ErrorResponseProcessor<ErrorInfo> {
    private static final String BASE_ERROR_CODE = "BASE-ERROR-001";

    @Override
    public MutableHttpResponse<ErrorInfo> processResponse(ErrorContext errorContext, MutableHttpResponse<?> response) {
        UUID errorUUID = UUID.randomUUID();
        log(errorContext, errorUUID);
        Throwable cause = errorContext.getRootCause().orElse(null);
        ErrorInfo errorInfo = ErrorInfo.builder()
            .errorMessage(response.reason())
            .errorCode(BASE_ERROR_CODE)
            .errorCause(cause == null ? null : String.valueOf(cause))
            .traceId(errorUUID.toString())
            .build();
        return response.body(errorInfo);
    }

    private void log(ErrorContext errorContext, UUID uuid) {
        if (errorContext.hasErrors()) {
            String message = errorContext.getErrors().stream().map(Error::getMessage).collect(Collectors.joining("\n", "", "\nuuid: {}"));
            errorContext.getRootCause().ifPresentOrElse(e -> log.error(message, uuid, e), () -> log.error(message, uuid));
        }
    }
}
