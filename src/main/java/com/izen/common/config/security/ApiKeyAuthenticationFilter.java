package com.izen.common.config.security;

import com.izen.common.api.response.ErrorResponseDto;
import com.izen.common.api.type.ResponseCode;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

@Component
public class ApiKeyAuthenticationFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;
    private final String secretApiKey;
    private static final List<String> EXCLUDED_URIS = List.of(
            "/api/v1/auth/logout",
            "/swagger-ui/**",
            "/api-docs/**"
    );
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    public ApiKeyAuthenticationFilter(
            ObjectMapper objectMapper,
            @Value("${security.api-key}") String secretApiKey
    ) {
        this.objectMapper = objectMapper;
        this.secretApiKey = secretApiKey;
    }

    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) throws ServletException {
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        String requestURI = request.getRequestURI();
        return EXCLUDED_URIS.stream()
                .anyMatch(pattern -> pathMatcher.match(pattern, requestURI));
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        String apiKeyHeader = request.getHeader("X-API-Key");

        if (apiKeyHeader != null && !secretApiKey.equals(apiKeyHeader)) {
            ResponseCode responseCode = ResponseCode.KEY_ERROR;

            ErrorResponseDto errorResponse = ErrorResponseDto.from(responseCode);

            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(responseCode.getStatus().value());
            response.getWriter().write(objectMapper.writeValueAsString(errorResponse));

            return;
        }

        filterChain.doFilter(request, response);
    }

}
