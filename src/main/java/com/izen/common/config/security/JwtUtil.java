package com.izen.common.config.security;

import com.izen.common.api.exception.CustomException;
import com.izen.common.api.type.ResponseCode;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class JwtUtil {
    private final JwtProvider jwtProvider;

    public String extractTokenForFilter(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header == null || header.isBlank()) return null;

        if (!header.startsWith("Bearer ")) return null;

        return header.substring(7).trim();
    }

    public String extractToken(HttpServletRequest request) {
        String token = extractTokenForFilter(request);
        if (token == null || token.isBlank()) {
            throw new CustomException(ResponseCode.UNAUTHORIZED);
        }
        return token;
    }

    public Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()
                || authentication instanceof AnonymousAuthenticationToken) {
            throw new CustomException(ResponseCode.UNAUTHORIZED);
        }

        return Long.parseLong(authentication.getName());
    }

    public Long extractUserIdFromRefresh(String token) {
        Claims claims = jwtProvider.getClaims(token, true);
        return Long.parseLong(claims.getSubject());
    }
}