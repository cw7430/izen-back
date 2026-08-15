package com.izen.auth;

import com.izen.BaseIntegrationTest;
import com.izen.common.api.type.ResponseCode;
import com.izen.module.auth.dto.request.LoginRequestDto;
import com.izen.module.auth.dto.request.UpdatePasswordRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthControllerUpdatePasswordTest extends BaseIntegrationTest {
    private static final LoginRequestDto loginData = new LoginRequestDto(
            "EMP003",
            "EMP003",
            false
    );

    @Test
    @DisplayName("비밀번호 변경 - 성공")
    void updatePasswordSuccess() throws Exception {
        String accessToken = authTestUtil.getTestToken(loginData).accessToken();
        UpdatePasswordRequestDto updateData = new UpdatePasswordRequestDto(
                "EMP003",
                "password1234$%"
        );
        patch("/api/v1/auth/password")
                .key().auth(accessToken).body(updateData)
                .send().andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("비밀번호 변경 - 잘 못된 입력 값")
    void updatePasswordFailWithValidationError() throws Exception {
        String accessToken = authTestUtil.getTestToken(loginData).accessToken();
        UpdatePasswordRequestDto updateData = new UpdatePasswordRequestDto(
                "EMP003",
                "123"
        );
        patch("/api/v1/auth/password")
                .key().auth(accessToken).body(updateData)
                .send().andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ResponseCode.VALIDATION_ERROR.getCode()))
                .andExpect(jsonPath("$.message").isNotEmpty());
    }

    @Test
    @DisplayName("비밀번호 변경 - 잘못 된 토큰")
    void updatePasswordFailWithInvalidToken() throws Exception {
        UpdatePasswordRequestDto updateData = new UpdatePasswordRequestDto(
                "EMP003",
                "password1234$%"
        );
        patch("/api/v1/auth/password")
                .key().auth("123dj3w989kp2ekohoiysofhawioerq87retreheiogujigbydfggauid").body(updateData)
                .send().andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value(ResponseCode.INVALID_TOKEN.getCode()))
                .andExpect(jsonPath("$.message").isNotEmpty());
    }

    @Test
    @DisplayName("비밀번호 변경 - 만료 된 토큰")
    void updatePasswordFailWithExpiredToken() throws Exception {
        String accessToken = authTestUtil.generateExpiredAccessToken(loginData);
        UpdatePasswordRequestDto updateData = new UpdatePasswordRequestDto(
                "EMP003",
                "password1234$%"
        );
        patch("/api/v1/auth/password")
                .key().auth(accessToken).body(updateData)
                .send().andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value(ResponseCode.EXPIRED_TOKEN.getCode()))
                .andExpect(jsonPath("$.message").isNotEmpty());
    }

    @Test
    @DisplayName("비밀번호 변경 - 인증 오류")
    void updatePasswordFailWithUnauthorized() throws Exception {
        UpdatePasswordRequestDto updateData = new UpdatePasswordRequestDto(
                "EMP003",
                "password1234$%"
        );
        patch("/api/v1/auth/password")
                .key().body(updateData)
                .send().andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value(ResponseCode.UNAUTHORIZED.getCode()))
                .andExpect(jsonPath("$.message").isNotEmpty());
    }

    @Test
    @DisplayName("비밀번호 변경 - 비밀번호 오류")
    void updatePasswordFailWithPasswordError() throws Exception {
        String accessToken = authTestUtil.getTestToken(loginData).accessToken();
        UpdatePasswordRequestDto updateData = new UpdatePasswordRequestDto(
                "EMP009",
                "password1234$%"
        );
        patch("/api/v1/auth/password")
                .key().auth(accessToken).body(updateData)
                .send().andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value(ResponseCode.PASSWORD_ERROR.getCode()))
                .andExpect(jsonPath("$.message").isNotEmpty());
    }

    @Test
    @DisplayName("비밀번호 변경 - Api Key 오류")
    void updatePasswordFailWithKeyError() throws Exception {
        String accessToken = authTestUtil.getTestToken(loginData).accessToken();
        UpdatePasswordRequestDto updateData = new UpdatePasswordRequestDto(
                "EMP003",
                "password1234$%"
        );
        patch("/api/v1/auth/password")
                .auth(accessToken).body(updateData)
                .send().andExpect(status().isForbidden())
                .andExpect(jsonPath("$.code").value(ResponseCode.KEY_ERROR.getCode()))
                .andExpect(jsonPath("$.message").isNotEmpty());
    }
}
