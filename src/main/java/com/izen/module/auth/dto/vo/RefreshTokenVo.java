package com.izen.module.auth.dto.vo;

import java.time.Instant;

public record RefreshTokenVo(
        Long refreshTokenId,
        Long accountId,
        String token,
        Instant expiresAt
) {
}
