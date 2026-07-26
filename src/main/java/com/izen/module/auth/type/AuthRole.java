package com.izen.module.auth.type;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.izen.common.api.exception.CustomException;
import com.izen.common.api.type.ResponseCode;

public enum AuthRole {
    USER, ADMIN, LEFT;

    @JsonValue
    public String getValue() {
        return name();
    }

    @JsonCreator
    public static AuthRole from(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            return AuthRole.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new CustomException(
                    ResponseCode.VALIDATION_ERROR,
                    "authRole",
                    "입력 가능값: USER, ADMIN, LEFT, 입력된 값: " + value
            );
        }
    }
}
