package com.cyolo.words.frequency.evaluator.log;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.http.annotation.Filter;
import io.micronaut.http.filter.HttpServerFilter;
import io.micronaut.http.filter.ServerFilterChain;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import reactor.core.publisher.Mono;

@Filter("${server.filter.path}")
@Singleton
@RequiredArgsConstructor
public class HttpLoggingFilter implements HttpServerFilter {
    private static final String API_PATH = "/api";

    private final Logger log = LoggerFactory.getLogger(HttpLoggingFilter.class);
    private final MeterRegistry meterRegistry;

    @Override
    public Publisher<MutableHttpResponse<?>> doFilter(HttpRequest<?> request, ServerFilterChain chain) {
        Instant startTime = Instant.now();
        return Mono.from(chain.proceed(request)).doOnSubscribe(subscription -> logRequest(request))
            .map(mutableHttpResponse -> {
                long processingTime = Duration.between(startTime, Instant.now()).toMillis();
                String requestPathTemplate = request.getAttribute(LoggingConstants.ROUTE_TEMPLATE_ATTRIBUTE, String.class)
                    .orElse(request.getPath());
                recordDuration(requestPathTemplate, processingTime);
                MDC.put("processing_time", String.valueOf(processingTime));
                logResponse(mutableHttpResponse, request);
                MDC.clear();
                return mutableHttpResponse;
            });
    }

    private void addLoggingDataToMdc(HttpRequest<?> request) {
        HttpHeaders headers = request.getHeaders();
        String traceId = headers.get(LoggingConstants.TRACE_ID);
        if (traceId == null) {
            traceId = UUID.randomUUID().toString();
        }
        MDC.put(LoggingConstants.TRACE_ID, traceId);
        String uri = String.valueOf(request.getUri());
        MDC.put(LoggingConstants.URI, uri);
        String method = String.valueOf(request.getMethod());
        MDC.put(LoggingConstants.METHOD, method);
        String operation = String.valueOf(request.getAttribute(LoggingConstants.ROUTE_TEMPLATE_ATTRIBUTE, String.class).orElse(null));
        MDC.put(LoggingConstants.OPERATION, operation);
        String userAgent = headers.get(HttpHeaders.USER_AGENT);
        MDC.put(LoggingConstants.USER_AGENT, userAgent);
    }

    private void recordDuration(String requestPathTemplate, long processingTime) {
        Timer duration = Timer
            .builder(LoggingConstants.REQUESTS_DURATION)
            .tag(LoggingConstants.PATH_TAG, requestPathTemplate)
            .publishPercentiles(0.5, 0.75, 0.95, 0.99)
            .register(meterRegistry);
        duration.record(processingTime, TimeUnit.MILLISECONDS);
    }

    private void logRequest(HttpRequest<?> httpRequest) {
        if (httpRequest.getUri().getPath().contains(API_PATH)) {
            addLoggingDataToMdc(httpRequest);
            String requestPathTemplate = httpRequest.getAttribute(LoggingConstants.ROUTE_TEMPLATE_ATTRIBUTE, String.class)
                .orElse(httpRequest.getPath());
            meterRegistry.counter(LoggingConstants.REQUESTS_COUNTER, LoggingConstants.PATH_TAG, requestPathTemplate).increment();
            log.info(LoggingConstants.REQUEST_DEFAULT_MESSAGE + MDC.getCopyOfContextMap());
        }
    }

    private void logResponse(MutableHttpResponse<?> httpResponse, HttpRequest<?> httpRequest) {
        String requestPath = httpRequest.getPath();
        if (requestPath.contains(API_PATH)) {
            MDC.put(LoggingConstants.HTTP_STATUS, String.valueOf(httpResponse.getStatus().getCode()));
            String requestPathTemplate = httpRequest.getAttribute(LoggingConstants.ROUTE_TEMPLATE_ATTRIBUTE, String.class)
                .orElse(requestPath);
            log.info(LoggingConstants.RESPONSE_DEFAULT_MESSAGE + MDC.getCopyOfContextMap());
            if (httpResponse.getStatus().getCode() >= 400) {
                meterRegistry.counter(LoggingConstants.REQUESTS_ERRORS_TOTAL, "path", requestPathTemplate).increment();
            }
        }
    }
}
