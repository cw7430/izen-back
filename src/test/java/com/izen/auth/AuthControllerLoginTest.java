package com.izen.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.izen.BaseIntegrationTest;
import com.izen.module.auth.dto.request.LoginRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@Transactional
public class AuthControllerLoginTest extends BaseIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AuthTestUtil authTestUtil;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("로그인 - 성공")
    void loginSuccess() throws Exception {
        LoginRequestDto data = new LoginRequestDto(
                "EMP003",
                "EMP003",
                false
        );
        mockMvc.perform(
                        post("/api/v1/auth/login")
                                .header("X-API-Key", authTestUtil.getTestApiKey())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(data))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").isNotEmpty())
                .andExpect(jsonPath("$.refreshToken").isNotEmpty());
    }

    @Test
    @DisplayName("로그인 - 잘못 된 요청")
    void loginFailWithValidationError() throws Exception {
        LoginRequestDto data = new LoginRequestDto(
                null,
                null,
                false
        );

        mockMvc.perform(
                        post("/api/v1/auth/login")
                                .header("X-API-Key", authTestUtil.getTestApiKey())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(data))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VE"))
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

        mockMvc.perform(
                        post("/api/v1/auth/login")
                                .header("X-API-Key", authTestUtil.getTestApiKey())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(data))
                )
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value("LGE"))
                .andExpect(jsonPath("$.message").isNotEmpty());
    }

    @Test
    @DisplayName("로그인 - 잘못 된 API-KEY")
    void loginFailWithKeyError() throws Exception {
        LoginRequestDto data = new LoginRequestDto(
                "EMP002",
                "EMP002",
                false
        );

        mockMvc.perform(
                        post("/api/v1/auth/login")
                                .header("X-API-Key", "api-key")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(data))
                )
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.code").value("KE"))
                .andExpect(jsonPath("$.message").isNotEmpty());
    }
}
