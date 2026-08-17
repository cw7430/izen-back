package com.izen.auth;

import com.izen.module.auth.dto.request.LogoutRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthControllerLogoutTest extends AuthControllerTest {
    private static final String URL = AUTH_URL + "/logout";

    @Test
    @DisplayName("로그아웃 - 성공")
    void logoutSuccess() throws Exception {
        String refreshToken = authTestUtil.getTestToken(MASTER_LOGIN_DATA).refreshToken();
        LogoutRequestDto data = new LogoutRequestDto(refreshToken);
        post(URL)
                .key().body(data).send()
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("로그아웃 - 잘못 된 토큰으로 성공")
    void logoutSuccessWithInvalidToken() throws Exception {
        LogoutRequestDto data = new LogoutRequestDto(INVALID_TOKEN);
        post(URL)
                .key().body(data).send()
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("로그아웃 - 토큰 없이 성공")
    void logoutSuccessWithoutToken() throws Exception {
        LogoutRequestDto data = new LogoutRequestDto(null);
        post(URL)
                .key().body(data).send()
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("로그아웃 - Api Key 없이 성공")
    void logoutSuccessWithoutKey() throws Exception {
        String refreshToken = authTestUtil.getTestToken(MASTER_LOGIN_DATA).refreshToken();
        LogoutRequestDto data = new LogoutRequestDto(refreshToken);
        post(URL)
                .body(data).send()
                .andExpect(status().isNoContent());
    }
}
