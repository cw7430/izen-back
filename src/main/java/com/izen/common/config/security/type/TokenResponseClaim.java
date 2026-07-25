package com.izen.common.config.security.type;

public record TokenResponseClaim(
        String token,
        Long expiresAtMs
) {
}
