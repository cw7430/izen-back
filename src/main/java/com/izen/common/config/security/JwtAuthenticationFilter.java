package com.izen.common.config.security;

import com.izen.common.config.security.type.Role;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtProvider jwtProvider;
    private final JwtUtil jwtUtil;

    private static final String REFRESH_URI = "/api/v1/auth/refresh";

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            String token = jwtUtil.extractTokenForFilter(request);

            if (token == null) {
                filterChain.doFilter(request, response);
                return;
            }

            boolean isRefreshPath = REFRESH_URI.equals((request.getRequestURI()));
            Claims claims = jwtProvider.getClaims(token, isRefreshPath);

            if (isRefreshPath) {
                request.setAttribute("refreshClaims", claims);
            } else {
                setAuthentication(claims);
            }

        } catch (Exception e) {
            request.setAttribute("exception", e);
        }
        filterChain.doFilter(request, response);
    }

    private void setAuthentication(Claims claims) {
        String id = claims.getSubject();
        String roleCode = claims.get("role", String.class);
        Role role = Role.from(roleCode);
        List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(role.getAuthority()));
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(id, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}