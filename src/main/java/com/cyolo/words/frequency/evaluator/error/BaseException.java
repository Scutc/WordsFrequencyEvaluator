package com.cyolo.words.frequency.evaluator.error;

import lombok.Getter;

@Getter
public class BaseException extends RuntimeException {
    private final BaseError error;
    private final String[] params;

    public BaseException(BaseError error, String... params) {
        super(error.getCode());
        this.error = error;
        this.params = params;
    }

    @Override
    public String getMessage() {
        return error.getCode() + " " + String.format(error.getDescription(), (Object[]) params);
    }
}
