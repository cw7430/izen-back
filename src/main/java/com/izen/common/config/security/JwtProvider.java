package com.izen.common.config.security;

import com.izen.common.api.exception.CustomException;
import com.izen.common.api.type.ResponseCode;
import com.izen.common.config.security.type.TokenResponseClaim;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.util.Date;


@Component
@Slf4j
public class JwtProvider {
    private final SecretKey accessSecretKey;
    private final Long accessTokenExpireTime;
    private final SecretKey refreshSecretKey;
    private final Long refreshTokenExpireTime;

    public JwtProvider(
            @Value("${jwt.access.secret}") String accessSecretKey,
            @Value("${jwt.access.expiration}") Duration accessTokenExpireTime,
            @Value("${jwt.refresh.secret}") String refreshSecretKey,
            @Value("${jwt.refresh.expiration}") Duration refreshTokenExpireTime
    ) {
        this.accessSecretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(accessSecretKey));
        this.accessTokenExpireTime = accessTokenExpireTime.toMillis();
        this.refreshSecretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(refreshSecretKey));
        this.refreshTokenExpireTime = refreshTokenExpireTime.toMillis();
    }

    public TokenResponseClaim generateAccessToken(String userId, String role) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + accessTokenExpireTime);

        String token = Jwts.builder()
                .subject(userId)
                .claim("role", role)
                .issuedAt(now)
                .expiration(expiry)
                .signWith(accessSecretKey, Jwts.SIG.HS256)
                .compact();

        return new TokenResponseClaim(token, expiry.getTime());
    }

    public TokenResponseClaim generateRefreshToken(String userId, boolean isAuto) {
        Date now = new Date();
        Date expiry = isAuto ?
                new Date(now.getTime() + Duration.ofDays(365).toMillis()) :
                new Date(now.getTime() + refreshTokenExpireTime);

        String token = Jwts.builder()
                .subject(userId)
                .issuedAt(now)
                .expiration(expiry)
                .signWith(refreshSecretKey, Jwts.SIG.HS256)
                .compact();

        return new TokenResponseClaim(token, expiry.getTime());
    }

    public Claims getClaims(String token, boolean isRefresh) {
        SecretKey key = isRefresh ? refreshSecretKey : accessSecretKey;

        try {
            return Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException e) {
            throw new CustomException(ResponseCode.EXPIRED_TOKEN);
        } catch (JwtException | IllegalArgumentException e) {
            throw new CustomException(ResponseCode.INVALID_TOKEN);
        }
    }
}
