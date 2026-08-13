package com.izen.auth;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.izen.BaseIntegrationTest;
import com.izen.module.auth.dto.request.CheckUserRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthControllerCheckUserTest extends BaseIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AuthTestUtil authTestUtil;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("아이디 중복체크 - 성공")
    void checkUserSuccess() throws Exception {
        CheckUserRequestDto data = new CheckUserRequestDto("user1234");
        mockMvc.perform(
                        post("/api/v1/auth/check-user")
                                .header("X-API-Key", authTestUtil.getTestApiKey())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(data))
                )
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("아이디 중복체크 - 잘 못된 입력 값")
    void checkUserFailWithValidationError() throws Exception {
        CheckUserRequestDto data = new CheckUserRequestDto("123");
        mockMvc.perform(
                        post("/api/v1/auth/check-user")
                                .header("X-API-Key", authTestUtil.getTestApiKey())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(data))
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VE"))
                .andExpect(jsonPath("$.message").isNotEmpty());
    }

    @Test
    @DisplayName("아이디 중복체크 - 아이디 중복")
    void checkUserFailWithDuplicateResource() throws Exception {
        CheckUserRequestDto data = new CheckUserRequestDto("EMP003");
        mockMvc.perform(
                        post("/api/v1/auth/check-user")
                                .header("X-API-Key", authTestUtil.getTestApiKey())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(data))
                )
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code").value("DR"))
                .andExpect(jsonPath("$.message").isNotEmpty());
    }

    @Test
    @DisplayName("아이디 중복체크 - Api Key 오류")
    void checkUserFailWithKeyError() throws Exception {
        CheckUserRequestDto data = new CheckUserRequestDto("user1234");
        mockMvc.perform(
                        post("/api/v1/auth/check-user")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(data))
                )
                .andDo(print())
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.code").value("KE"))
                .andExpect(jsonPath("$.message").isNotEmpty());
    }
}
