package com.izen.auth;

import com.izen.common.api.type.ResponseCode;
import com.izen.module.auth.dto.request.UpdatePasswordRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthControllerUpdatePasswordTest extends AuthControllerTest {

    private static final String URL = AUTH_URL + "/password";
    private static final UpdatePasswordRequestDto DATA = new UpdatePasswordRequestDto(
            "EMP003",
            "password1234$%"
    );
    private static final UpdatePasswordRequestDto INVALID_DATA = new UpdatePasswordRequestDto(
            "EMP003",
            "123"
    );
    private static final UpdatePasswordRequestDto WRONG_PASSWORD_DATA = new UpdatePasswordRequestDto(
            "EMP009",
            "password1234$%"
    );

    @Test
    @DisplayName("비밀번호 변경 - 성공")
    void updatePasswordSuccess() throws Exception {
        String accessToken = authTestUtil.getTestToken(MASTER_LOGIN_DATA).accessToken();
        patch(URL)
                .key().auth(accessToken).body(DATA)
                .send().andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("비밀번호 변경 - 잘 못된 입력 값")
    void updatePasswordFailWithValidationError() throws Exception {
        String accessToken = authTestUtil.getTestToken(MASTER_LOGIN_DATA).accessToken();
        patch(URL)
                .key().auth(accessToken).body(INVALID_DATA)
                .send().andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ResponseCode.VALIDATION_ERROR.getCode()))
                .andExpect(jsonPath("$.message").isNotEmpty());
    }

    @Test
    @DisplayName("비밀번호 변경 - 잘못 된 토큰")
    void updatePasswordFailWithInvalidToken() throws Exception {
        patch(URL)
                .key().auth(INVALID_TOKEN).body(DATA)
                .send().andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value(ResponseCode.INVALID_TOKEN.getCode()))
                .andExpect(jsonPath("$.message").isNotEmpty());
    }

    @Test
    @DisplayName("비밀번호 변경 - 만료 된 토큰")
    void updatePasswordFailWithExpiredToken() throws Exception {
        String accessToken = authTestUtil.generateExpiredAccessToken(MASTER_LOGIN_DATA);
        patch(URL)
                .key().auth(accessToken).body(DATA)
                .send().andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value(ResponseCode.EXPIRED_TOKEN.getCode()))
                .andExpect(jsonPath("$.message").isNotEmpty());
    }

    @Test
    @DisplayName("비밀번호 변경 - 인증 오류")
    void updatePasswordFailWithUnauthorized() throws Exception {
        patch(URL)
                .key().body(DATA)
                .send().andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value(ResponseCode.UNAUTHORIZED.getCode()))
                .andExpect(jsonPath("$.message").isNotEmpty());
    }

    @Test
    @DisplayName("비밀번호 변경 - 비밀번호 오류")
    void updatePasswordFailWithPasswordError() throws Exception {
        String accessToken = authTestUtil.getTestToken(MASTER_LOGIN_DATA).accessToken();
        patch(URL)
                .key().auth(accessToken).body(WRONG_PASSWORD_DATA)
                .send().andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value(ResponseCode.PASSWORD_ERROR.getCode()))
                .andExpect(jsonPath("$.message").isNotEmpty());
    }

    @Test
    @DisplayName("비밀번호 변경 - Api Key 오류")
    void updatePasswordFailWithKeyError() throws Exception {
        String accessToken = authTestUtil.getTestToken(MASTER_LOGIN_DATA).accessToken();
        patch(URL)
                .auth(accessToken).body(DATA)
                .send().andExpect(status().isForbidden())
                .andExpect(jsonPath("$.code").value(ResponseCode.KEY_ERROR.getCode()))
                .andExpect(jsonPath("$.message").isNotEmpty());
    }
}
