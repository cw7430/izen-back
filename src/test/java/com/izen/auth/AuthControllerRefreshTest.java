package com.izen.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.izen.BaseIntegrationTest;
import com.izen.module.auth.dto.request.LoginRequestDto;
import com.izen.module.auth.dto.request.RefreshRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@Transactional
public class AuthControllerRefreshTest extends BaseIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AuthTestUtil authTestUtil;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("토큰 재발급 - 성공")
    void refreshSuccess() throws Exception {
        LoginRequestDto loginData = new LoginRequestDto(
                "EMP003",
                "EMP003",
                false
        );
        RefreshRequestDto refreshData = new RefreshRequestDto(false);
        mockMvc.perform(
                        post("/api/v1/auth/refresh")
                                .header("X-API-Key", authTestUtil.getTestApiKey())
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + authTestUtil.getTestToken(loginData).refreshToken())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(refreshData))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").isNotEmpty())
                .andExpect(jsonPath("$.refreshToken").isNotEmpty());
    }

}
