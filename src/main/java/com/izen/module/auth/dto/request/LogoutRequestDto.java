package com.izen.module.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "LogoutRequest")
public record LogoutRequestDto(
        @Schema(description = "Refresh Token", nullable = true)
        String refreshToken
) {
}
