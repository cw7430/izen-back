package com.izen.common.api.type;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.jspecify.annotations.NonNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

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

    @Component
    public static class ConverterImpl implements Converter<String, SortOrder> {
        @Override
        public SortOrder convert(@NonNull String source) {
            return SortOrder.from(source);
        }
    }
}
