package com.izen.auth;

import com.izen.module.auth.AuthService;
import com.izen.module.auth.dto.request.LoginRequestDto;
import com.izen.module.auth.dto.response.LoginResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AuthTestUtil {
    @Autowired
    private AuthService authService;

    @Value("${security.api-key}")
    private String apiKey;

    public LoginResponseDto getTestToken(LoginRequestDto reqDto) {
        return authService.login(reqDto);
    }

    public String getTestApiKey() {
        return apiKey;
    }
}
