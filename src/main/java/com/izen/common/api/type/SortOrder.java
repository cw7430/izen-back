package com.izen.common.api.type;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum SortOrder {
    ASC, DESC;

    @JsonValue
    public String getValue() {
        return name();
    }

    @JsonCreator
    public static SortOrder from(String value) {
        if (value == null || value.isBlank()) {
            return SortOrder.ASC;
        }
        try {
            return SortOrder.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            return SortOrder.ASC;
        }
    }
}
