package com.izen.common.api.response;

import com.izen.common.api.type.ResponseCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class ErrorResponseDto {
    @Getter
    @AllArgsConstructor
    protected static final class Simple extends ErrorResponseDto {
        private final String code;
        private final String message;
    }

    @Getter
    @AllArgsConstructor
    protected static final class WithErrors<T> extends ErrorResponseDto {
        private final String code;
        private final String message;
        private final T errors;
    }

    public static ErrorResponseDto from(ResponseCode responseCode) {
        return new Simple(responseCode.getCode(), responseCode.getMessage());
    }

    public static <T> ErrorResponseDto of(ResponseCode responseCode, T errors) {
        return new WithErrors<>(responseCode.getCode(), responseCode.getMessage(), errors);
    }
}
