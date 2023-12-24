package com.cyolo.error;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.server.exceptions.ExceptionHandler;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

import static com.cyolo.error.CommonError.SERVICE_ERROR;

@Singleton
@Slf4j
@RequiredArgsConstructor
public class BaseExceptionHandler implements ExceptionHandler<RuntimeException, HttpResponse<ErrorInfo>> {
    private static final String ERROR_CODE = "error_code";
    private static final String ERROR_DESCRIPTION = "error_description";

    @Override
    public HttpResponse<ErrorInfo> handle(HttpRequest request, RuntimeException exception) {
        if (exception instanceof BaseException baseException) {
            if (baseException.getCause() == null) {
                log.error(baseException.getMessage());
            } else {
                log.error(baseException.getMessage(), baseException);
            }
            return createResponse(baseException);
        } else {
            log.error(exception.getMessage(), exception);
            return createResponse(SERVICE_ERROR, exception);
        }
    }

    private HttpResponse<ErrorInfo> createResponse(BaseException exception) {
        return createResponse(exception.getError(), exception.getCause(), exception.getParams());
    }

    private HttpResponse<ErrorInfo> createResponse(BaseError error, Throwable cause, String... params) {
        String traceId = MDC.get("trace_id");
        String description = String.format(error.getDescription(), (Object[]) params);
        ErrorInfo errorInfo = ErrorInfo.builder()
            .errorMessage(description)
            .errorCode(error.getCode())
            .errorCause(cause == null ? null : String.valueOf(cause))
            .traceId(traceId)
            .build();
        return HttpResponse.status(error.getHttpStatus()).body(errorInfo);
    }
}
