package com.izen.common.config.security.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Role {
    USER("USER", "ROLE_USER"), ADMIN("ADMIN", "ROLE_ADMIN"), GUEST("GUEST", "ROLE_GUEST");

    private final String code;
    private final String authority;

    public static Role from(String code) {
        if (code == null || code.isBlank()) {
            return Role.GUEST;
        }
        try {
            return Role.valueOf(code.toUpperCase());
        } catch (IllegalArgumentException e) {
            return Role.GUEST;
        }
    }
}
