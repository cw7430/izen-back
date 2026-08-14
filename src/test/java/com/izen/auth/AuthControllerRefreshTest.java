package com.izen.auth;

import com.izen.BaseIntegrationTest;
import com.izen.common.api.type.ResponseCode;
import com.izen.module.auth.dto.request.LoginRequestDto;
import com.izen.module.auth.dto.request.RefreshRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthControllerRefreshTest extends BaseIntegrationTest {

    private static final LoginRequestDto loginData = new LoginRequestDto(
            "EMP003",
            "EMP003",
            false
    );

    @Test
    @DisplayName("토큰 재발급 - 성공")
    void refreshSuccess() throws Exception {
        String refreshToken = authTestUtil.getTestToken(loginData).refreshToken();
        RefreshRequestDto refreshData = new RefreshRequestDto(false);
        post("/api/v1/auth/refresh")
                .key().auth(refreshToken).body(refreshData)
                .send().andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").isNotEmpty())
                .andExpect(jsonPath("$.refreshToken").isNotEmpty());
    }

    @Test
    @DisplayName("토큰 재발급 - 인증 오류")
    void refreshFailWithUnauthorized() throws Exception {
        RefreshRequestDto refreshData = new RefreshRequestDto(false);
        post("/api/v1/auth/refresh").key().body(refreshData)
                .send().andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value(ResponseCode.UNAUTHORIZED.getCode()))
                .andExpect(jsonPath("$.message").isNotEmpty());
    }

    @Test
    @DisplayName("토큰 재발급 - 잘못 된 토큰")
    void refreshFailWithInvalidToken() throws Exception {
        RefreshRequestDto refreshData = new RefreshRequestDto(false);
        post("/api/v1/auth/refresh").key()
                .auth("123dj3w989kp2ekohoiysofhawioerq87retreheiogujigbydfggauid").body(refreshData)
                .send().andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value(ResponseCode.INVALID_TOKEN.getCode()))
                .andExpect(jsonPath("$.message").isNotEmpty());
    }

    @Test
    @DisplayName("토큰 재발급 - 만료 된 토큰")
    void refreshFailWithExpiredToken() throws Exception {
        RefreshRequestDto refreshData = new RefreshRequestDto(false);
        String refreshToken = authTestUtil.generateExpiredRefreshToken(loginData);
        post("/api/v1/auth/refresh").key().auth(refreshToken).body(refreshData)
                .send().andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value(ResponseCode.EXPIRED_TOKEN.getCode()))
                .andExpect(jsonPath("$.message").isNotEmpty());

    }

    @Test
    @DisplayName("토큰 재발급 - Api Key 오류")
    void refreshFailWithKeyError() throws Exception {
        String refreshToken = authTestUtil.getTestToken(loginData).refreshToken();
        RefreshRequestDto refreshData = new RefreshRequestDto(false);
        post("/api/v1/auth/refresh")
                .auth(refreshToken).body(refreshData)
                .send().andExpect(status().isForbidden())
                .andExpect(jsonPath("$.code").value(ResponseCode.KEY_ERROR.getCode()))
                .andExpect(jsonPath("$.message").isNotEmpty());
    }

}
