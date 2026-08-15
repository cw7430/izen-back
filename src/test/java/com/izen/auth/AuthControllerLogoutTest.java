package com.izen.auth;

import com.izen.BaseIntegrationTest;
import com.izen.module.auth.dto.request.LoginRequestDto;
import com.izen.module.auth.dto.request.LogoutRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthControllerLogoutTest extends BaseIntegrationTest {
    private static final LoginRequestDto loginData = new LoginRequestDto(
            "EMP003",
            "EMP003",
            false
    );

    @Test
    @DisplayName("로그아웃 - 성공")
    void logoutSuccess() throws Exception {
        String refreshToken = authTestUtil.getTestToken(loginData).refreshToken();
        LogoutRequestDto data = new LogoutRequestDto(refreshToken);
        post("/api/v1/auth/logout")
                .key().body(data).send()
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("로그아웃 - 잘못 된 토큰으로 성공")
    void logoutSuccessWithInvalidToken() throws Exception {
        LogoutRequestDto data = new LogoutRequestDto("123dj3w989kp2ekohoiysofhawioerq87retreheiogujigbydfggauid");
        post("/api/v1/auth/logout")
                .key().body(data).send()
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("로그아웃 - 토큰 없이 성공")
    void logoutSuccessWithoutToken() throws Exception {
        LogoutRequestDto data = new LogoutRequestDto(null);
        post("/api/v1/auth/logout")
                .key().body(data).send()
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("로그아웃 - Api Key 없이 성공")
    void logoutSuccessWithoutKey() throws Exception {
        String refreshToken = authTestUtil.getTestToken(loginData).refreshToken();
        LogoutRequestDto data = new LogoutRequestDto(refreshToken);
        post("/api/v1/auth/logout")
                .body(data).send()
                .andExpect(status().isNoContent());
    }
}
