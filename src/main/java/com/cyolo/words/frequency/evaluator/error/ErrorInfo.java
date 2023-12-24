package com.cyolo.words.frequency.evaluator.error;

import io.micronaut.serde.annotation.Serdeable;
import lombok.Builder;

@Serdeable
@Builder
public record ErrorInfo(String errorMessage, String errorCode, String errorCause, String traceId) {
}
