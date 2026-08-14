package com.izen.auth;

import com.izen.BaseIntegrationTest;
import com.izen.common.api.type.ResponseCode;
import com.izen.module.auth.dto.request.LoginRequestDto;
import com.izen.module.auth.dto.request.RefreshRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthControllerRefreshTest extends BaseIntegrationTest {

    @Test
    @DisplayName("토큰 재발급 - 성공")
    void refreshSuccess() throws Exception {
        LoginRequestDto loginData = new LoginRequestDto(
                "EMP003",
                "EMP003",
                false
        );
        String refreshToken = authTestUtil.getTestToken(loginData).refreshToken();
        RefreshRequestDto refreshData = new RefreshRequestDto(false);
        mockMvc.perform(
                        post("/api/v1/auth/refresh")
                                .header("X-API-Key", authTestUtil.getTestApiKey())
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + refreshToken)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(refreshData))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").isNotEmpty())
                .andExpect(jsonPath("$.refreshToken").isNotEmpty());
    }

    @Test
    @DisplayName("토큰 재발급 - 인증 오류")
    void refreshFailWithUnauthorized() throws Exception {
        RefreshRequestDto refreshData = new RefreshRequestDto(false);
        mockMvc.perform(
                        post("/api/v1/auth/refresh")
                                .header("X-API-Key", authTestUtil.getTestApiKey())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(refreshData))
                )
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value(ResponseCode.UNAUTHORIZED.getCode()))
                .andExpect(jsonPath("$.message").isNotEmpty());
    }

    @Test
    @DisplayName("토큰 재발급 - 잘못 된 토큰")
    void refreshFailWithInvalidToken() throws Exception {
        RefreshRequestDto refreshData = new RefreshRequestDto(false);
        mockMvc.perform(
                        post("/api/v1/auth/refresh")
                                .header("X-API-Key", authTestUtil.getTestApiKey())
                                .header(HttpHeaders.AUTHORIZATION, "Bearer 123dj3w989kp2ekohoiysofhawioerq87retreheiogujigbydfggauid")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(refreshData))
                )
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value(ResponseCode.INVALID_TOKEN.getCode()))
                .andExpect(jsonPath("$.message").isNotEmpty());
    }

    @Test
    @DisplayName("토큰 재발급 - 만료 된 토큰")
    void refreshFailWithExpiredToken() throws Exception {
        LoginRequestDto loginData = new LoginRequestDto(
                "EMP003",
                "EMP003",
                false
        );
        RefreshRequestDto refreshData = new RefreshRequestDto(false);
        String refreshToken = authTestUtil.generateExpiredRefreshToken(loginData);
        mockMvc.perform(
                        post("/api/v1/auth/refresh")
                                .header("X-API-Key", authTestUtil.getTestApiKey())
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + refreshToken)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(refreshData))
                )
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value(ResponseCode.EXPIRED_TOKEN.getCode()))
                .andExpect(jsonPath("$.message").isNotEmpty());

    }

    @Test
    @DisplayName("토큰 재발급 - Api Key 오류")
    void refreshFailWithKeyError() throws Exception {
        LoginRequestDto loginData = new LoginRequestDto(
                "EMP003",
                "EMP003",
                false
        );
        String refreshToken = authTestUtil.getTestToken(loginData).refreshToken();
        RefreshRequestDto refreshData = new RefreshRequestDto(false);
        mockMvc.perform(
                        post("/api/v1/auth/refresh")
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + refreshToken)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(refreshData))
                )
                .andDo(print())
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.code").value(ResponseCode.KEY_ERROR.getCode()))
                .andExpect(jsonPath("$.message").isNotEmpty());
    }

}
