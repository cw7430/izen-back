package com.izen.auth;

import com.izen.BaseIntegrationTest;
import com.izen.common.api.type.ResponseCode;
import com.izen.module.auth.dto.request.LoginRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthControllerLoginTest extends BaseIntegrationTest {

    @Test
    @DisplayName("로그인 - 성공")
    void loginSuccess() throws Exception {
        LoginRequestDto data = new LoginRequestDto(
                "EMP003",
                "EMP003",
                false
        );
        post("/api/v1/auth/login").key().body(data).send()
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").isNotEmpty())
                .andExpect(jsonPath("$.refreshToken").isNotEmpty());
    }

    @Test
    @DisplayName("로그인 - 잘 못된 입력 값")
    void loginFailWithValidationError() throws Exception {
        LoginRequestDto data = new LoginRequestDto(
                null,
                null,
                false
        );

        post("/api/v1/auth/login").key().body(data).send()
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ResponseCode.VALIDATION_ERROR.getCode()))
                .andExpect(jsonPath("$.message").isNotEmpty());
    }

    @Test
    @DisplayName("로그인 - 잘못 된 아이디 또는 비밀번호")
    void loginFailWithLoginError() throws Exception {
        LoginRequestDto data = new LoginRequestDto(
                "EMP002",
                "EMP002",
                false
        );

        post("/api/v1/auth/login").key().body(data).send()
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value(ResponseCode.LOGIN_ERROR.getCode()))
                .andExpect(jsonPath("$.message").isNotEmpty());
    }

    @Test
    @DisplayName("로그인 - 잘못 된 API-KEY")
    void loginFailWithKeyError() throws Exception {
        LoginRequestDto data = new LoginRequestDto(
                "EMP003",
                "EMP003",
                false
        );

        post("/api/v1/auth/login").body(data).send()
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.code").value(ResponseCode.KEY_ERROR.getCode()))
                .andExpect(jsonPath("$.message").isNotEmpty());
    }
}
