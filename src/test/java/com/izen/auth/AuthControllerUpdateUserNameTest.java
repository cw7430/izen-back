package com.izen.auth;

import com.izen.BaseIntegrationTest;
import com.izen.common.api.type.ResponseCode;
import com.izen.module.auth.dto.request.LoginRequestDto;
import com.izen.module.auth.dto.request.UpdateUserNameRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthControllerUpdateUserNameTest extends BaseIntegrationTest {

    @Test
    @DisplayName("아이디 변경 - 성공")
    void updateUserNameSuccess() throws Exception {
        LoginRequestDto loginData = new LoginRequestDto(
                "EMP003",
                "EMP003",
                false
        );
        String accessToken = authTestUtil.getTestToken(loginData).accessToken();
        UpdateUserNameRequestDto updateData = new UpdateUserNameRequestDto(
                "updateuser123",
                "EMP003"
        );
        mockMvc.perform(
                        patch("/api/v1/auth/user-name")
                                .header("X-API-Key", authTestUtil.getTestApiKey())
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(updateData))
                ).andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("아이디 변경 - 잘 못된 입력 값")
    void updateUserNameFailWithValidationError() throws Exception {
        LoginRequestDto loginData = new LoginRequestDto(
                "EMP003",
                "EMP003",
                false
        );
        String accessToken = authTestUtil.getTestToken(loginData).accessToken();
        UpdateUserNameRequestDto updateData = new UpdateUserNameRequestDto(
                "123",
                "EMP003"
        );
        mockMvc.perform(
                        patch("/api/v1/auth/user-name")
                                .header("X-API-Key", authTestUtil.getTestApiKey())
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(updateData))
                ).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ResponseCode.VALIDATION_ERROR.getCode()))
                .andExpect(jsonPath("$.message").isNotEmpty());
    }

    @Test
    @DisplayName("아이디 변경 - 잘못 된 토큰")
    void updateUserNameFailWithInvalidToken() throws Exception {
        UpdateUserNameRequestDto updateData = new UpdateUserNameRequestDto(
                "updateuser123",
                "EMP003"
        );
        mockMvc.perform(
                        patch("/api/v1/auth/user-name")
                                .header("X-API-Key", authTestUtil.getTestApiKey())
                                .header(HttpHeaders.AUTHORIZATION, "Bearer 123dj3w989kp2ekohoiysofhawioerq87retreheiogujigbydfggauid")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(updateData))
                ).andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value(ResponseCode.INVALID_TOKEN.getCode()))
                .andExpect(jsonPath("$.message").isNotEmpty());
    }

    @Test
    @DisplayName("아이디 변경 - 만료 된 토큰")
    void updateUserNameFailWithExpiredToken() throws Exception {
        LoginRequestDto loginData = new LoginRequestDto(
                "EMP003",
                "EMP003",
                false
        );
        String accessToken = authTestUtil.generateExpiredAccessToken(loginData);
        UpdateUserNameRequestDto updateData = new UpdateUserNameRequestDto(
                "updateuser123",
                "EMP003"
        );
        mockMvc.perform(
                        patch("/api/v1/auth/user-name")
                                .header("X-API-Key", authTestUtil.getTestApiKey())
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(updateData))
                ).andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value(ResponseCode.EXPIRED_TOKEN.getCode()))
                .andExpect(jsonPath("$.message").isNotEmpty());
    }

    @Test
    @DisplayName("아이디 변경 - 인증 오류")
    void updateUserNameFailWithUnauthorized() throws Exception {
        UpdateUserNameRequestDto updateData = new UpdateUserNameRequestDto(
                "updateuser123",
                "EMP003"
        );
        mockMvc.perform(
                        patch("/api/v1/auth/user-name")
                                .header("X-API-Key", authTestUtil.getTestApiKey())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(updateData))
                ).andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value(ResponseCode.UNAUTHORIZED.getCode()))
                .andExpect(jsonPath("$.message").isNotEmpty());
    }

    @Test
    @DisplayName("아이디 변경 - 비밀번호 오류")
    void updateUserNameFailWithPasswordError() throws Exception {
        LoginRequestDto loginData = new LoginRequestDto(
                "EMP003",
                "EMP003",
                false
        );
        String accessToken = authTestUtil.getTestToken(loginData).accessToken();
        UpdateUserNameRequestDto updateData = new UpdateUserNameRequestDto(
                "updateuser123",
                "123"
        );
        mockMvc.perform(
                        patch("/api/v1/auth/user-name")
                                .header("X-API-Key", authTestUtil.getTestApiKey())
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(updateData))
                ).andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value(ResponseCode.PASSWORD_ERROR.getCode()))
                .andExpect(jsonPath("$.message").isNotEmpty());
    }

    @Test
    @DisplayName("아이디 변경 - Api Key 오류")
    void updateUserNameFailWithKeyError() throws Exception {
        LoginRequestDto loginData = new LoginRequestDto(
                "EMP003",
                "EMP003",
                false
        );
        String accessToken = authTestUtil.getTestToken(loginData).accessToken();
        UpdateUserNameRequestDto updateData = new UpdateUserNameRequestDto(
                "updateuser123",
                "EMP003"
        );
        mockMvc.perform(
                        patch("/api/v1/auth/user-name")
                                .header("X-API-Key", "123ewegq3r3r213rh")
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(updateData))
                ).andDo(print())
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.code").value(ResponseCode.KEY_ERROR.getCode()))
                .andExpect(jsonPath("$.message").isNotEmpty());
    }

    @Test
    @DisplayName("아이디 변경 - 아이디 중복")
    void updateUserNameFailWithDuplicateResource() throws Exception {
        LoginRequestDto loginData = new LoginRequestDto(
                "EMP003",
                "EMP003",
                false
        );
        String accessToken = authTestUtil.getTestToken(loginData).accessToken();
        UpdateUserNameRequestDto updateData = new UpdateUserNameRequestDto(
                "EMP010",
                "EMP003"
        );
        mockMvc.perform(
                        patch("/api/v1/auth/user-name")
                                .header("X-API-Key", authTestUtil.getTestApiKey())
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(updateData))
                ).andDo(print())
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code").value(ResponseCode.DUPLICATE_RESOURCE.getCode()))
                .andExpect(jsonPath("$.message").isNotEmpty());
    }
}
