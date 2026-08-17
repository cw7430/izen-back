package com.izen.auth;

import com.izen.common.api.type.ResponseCode;
import com.izen.module.auth.dto.request.UpdateAccountRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthControllerUpdateAccountTest extends AuthControllerTest {
    private static final String URL = AUTH_URL + "/account";
    private static final UpdateAccountRequestDto DATA = new UpdateAccountRequestDto(
            "EMP003",
            "010-0000-0000",
            "email@email.com"
    );
    private static final UpdateAccountRequestDto INVALID_DATA = new UpdateAccountRequestDto(
            "EMP003",
            "123",
            "456"
    );
    private static final UpdateAccountRequestDto WRONG_PASSWORD_DATA = new UpdateAccountRequestDto(
            "EMP009",
            "010-0000-0000",
            "email@email.com"
    );

    @Test
    @DisplayName("계정정보 변경 - 성공")
    void updateAccountSuccess() throws Exception {
        String accessToken = authTestUtil.getTestToken(MASTER_LOGIN_DATA).accessToken();
        patch(URL)
                .key().auth(accessToken).body(DATA)
                .send().andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("계정정보 변경 - 잘 못된 입력 값")
    void updateAccountFailWithValidationError() throws Exception {
        String accessToken = authTestUtil.getTestToken(MASTER_LOGIN_DATA).accessToken();
        patch(URL)
                .key().auth(accessToken).body(INVALID_DATA)
                .send().andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ResponseCode.VALIDATION_ERROR.getCode()))
                .andExpect(jsonPath("$.message").isNotEmpty());
    }

    @Test
    @DisplayName("계정정보 변경 - 잘못 된 토큰")
    void updateAccountFailWithInvalidToken() throws Exception {
        patch(URL)
                .key().auth(INVALID_TOKEN).body(DATA)
                .send().andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value(ResponseCode.INVALID_TOKEN.getCode()))
                .andExpect(jsonPath("$.message").isNotEmpty());
    }

    @Test
    @DisplayName("계정정보 변경 - 만료 된 토큰")
    void updateAccountFailWithExpiredToken() throws Exception {
        String accessToken = authTestUtil.generateExpiredAccessToken(MASTER_LOGIN_DATA);
        patch(URL)
                .key().auth(accessToken).body(DATA)
                .send().andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value(ResponseCode.EXPIRED_TOKEN.getCode()))
                .andExpect(jsonPath("$.message").isNotEmpty());
    }

    @Test
    @DisplayName("계정정보 변경 - 인증 오류")
    void updateAccountFailWithUnauthorized() throws Exception {
        patch(URL)
                .key().body(DATA)
                .send().andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value(ResponseCode.UNAUTHORIZED.getCode()))
                .andExpect(jsonPath("$.message").isNotEmpty());
    }

    @Test
    @DisplayName("계정정보 변경 - 비밀번호 오류")
    void updateAccountFailWithPasswordError() throws Exception {
        String accessToken = authTestUtil.getTestToken(MASTER_LOGIN_DATA).accessToken();
        patch(URL)
                .key().auth(accessToken).body(WRONG_PASSWORD_DATA)
                .send().andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value(ResponseCode.PASSWORD_ERROR.getCode()))
                .andExpect(jsonPath("$.message").isNotEmpty());
    }

    @Test
    @DisplayName("계정정보 변경 - Api Key 오류")
    void updateAccountFailWithKeyError() throws Exception {
        String accessToken = authTestUtil.getTestToken(MASTER_LOGIN_DATA).accessToken();
        patch(URL)
                .auth(accessToken).body(DATA)
                .send().andExpect(status().isForbidden())
                .andExpect(jsonPath("$.code").value(ResponseCode.KEY_ERROR.getCode()))
                .andExpect(jsonPath("$.message").isNotEmpty());
    }
}
