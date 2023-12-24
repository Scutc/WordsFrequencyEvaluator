package com.cyolo.words.frequency.evaluator.error;

import com.cyolo.words.frequency.evaluator.log.LoggingConstants;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.server.exceptions.ExceptionHandler;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

@Singleton
@Slf4j
public class BaseExceptionHandler implements ExceptionHandler<RuntimeException, HttpResponse<ErrorInfo>> {
    @Override
    public HttpResponse<ErrorInfo> handle(HttpRequest request, RuntimeException exception) {
        if (exception instanceof BaseException baseException) {
            if (baseException.getCause() == null) {
                log.error(baseException.getMessage() + " " +  MDC.getCopyOfContextMap());
            } else {
                log.error(baseException.getMessage() + " " + MDC.getCopyOfContextMap(), baseException);
            }
            return createResponse(baseException);
        } else {
            log.error(exception.getMessage() + " " + MDC.getCopyOfContextMap(), exception);
            return createResponse(CommonError.SERVICE_ERROR, exception);
        }
    }

    private HttpResponse<ErrorInfo> createResponse(BaseException exception) {
        return createResponse(exception.getError(), exception.getCause(), exception.getParams());
    }

    private HttpResponse<ErrorInfo> createResponse(BaseError error, Throwable cause, String... params) {
        String traceId = MDC.get(LoggingConstants.TRACE_ID);
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
