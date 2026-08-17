package com.izen.auth;

import com.izen.common.api.type.ResponseCode;
import com.izen.module.auth.dto.request.LoginRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthControllerLoginTest extends AuthControllerTest {

    private static final String URL = AUTH_URL + "/login";
    private static final LoginRequestDto INVALID_LOGIN_DATA = new LoginRequestDto(
            null,
            null,
            false
    );
    private static final LoginRequestDto WRONG_LOGIN_DATA = new LoginRequestDto(
            "EMP002",
            "EMP002",
            false
    );

    @Test
    @DisplayName("로그인 - 성공")
    void loginSuccess() throws Exception {
        post(URL).key().body(MASTER_LOGIN_DATA).send()
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").isNotEmpty())
                .andExpect(jsonPath("$.refreshToken").isNotEmpty());
    }

    @Test
    @DisplayName("로그인 - 잘 못된 입력 값")
    void loginFailWithValidationError() throws Exception {
        post(URL).key().body(INVALID_LOGIN_DATA).send()
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ResponseCode.VALIDATION_ERROR.getCode()))
                .andExpect(jsonPath("$.message").isNotEmpty());
    }

    @Test
    @DisplayName("로그인 - 잘못 된 아이디 또는 비밀번호")
    void loginFailWithLoginError() throws Exception {
        post(URL).key().body(WRONG_LOGIN_DATA).send()
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value(ResponseCode.LOGIN_ERROR.getCode()))
                .andExpect(jsonPath("$.message").isNotEmpty());
    }

    @Test
    @DisplayName("로그인 - 잘못 된 API-KEY")
    void loginFailWithKeyError() throws Exception {
        post(URL).body(MASTER_LOGIN_DATA).send()
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.code").value(ResponseCode.KEY_ERROR.getCode()))
                .andExpect(jsonPath("$.message").isNotEmpty());
    }
}
