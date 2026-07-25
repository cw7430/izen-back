package com.izen.common.api.exception;

import com.izen.common.api.response.ErrorResponseDto;
import com.izen.common.api.type.ResponseCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final ObjectMapper objectMapper;

    @Override
    public void commence(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull AuthenticationException authException
    ) throws IOException, ServletException {
        Object exception = request.getAttribute("exception");

        ResponseCode responseCode;

        if (exception instanceof ResponseCode code) {
            responseCode = code;
        } else {
            responseCode = ResponseCode.UNAUTHORIZED;
        }

        ErrorResponseDto errorResponse = ErrorResponseDto.from(responseCode);

        response.setStatus(responseCode.getStatus().value());
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}