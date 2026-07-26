package com.izen.module.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "RefreshRequest")
public record RefreshRequestDto(
        @Schema(description = "장기 로그인", example = "false")
        boolean isAuto
) {
}
