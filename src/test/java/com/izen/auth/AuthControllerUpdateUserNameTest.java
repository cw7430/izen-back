package com.izen.auth;

import com.izen.common.api.type.ResponseCode;
import com.izen.module.auth.dto.request.UpdateUserNameRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthControllerUpdateUserNameTest extends AuthControllerTest {

    private static final String URL = AUTH_URL + "/user-name";

    @Test
    @DisplayName("아이디 변경 - 성공")
    void updateUserNameSuccess() throws Exception {
        String accessToken = authTestUtil.getTestToken(DEFAULT_LOGIN_DATA).accessToken();
        UpdateUserNameRequestDto updateData = new UpdateUserNameRequestDto(
                "updateuser123",
                "EMP003"
        );
        patch(URL)
                .key().auth(accessToken).body(updateData)
                .send().andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("아이디 변경 - 잘 못된 입력 값")
    void updateUserNameFailWithValidationError() throws Exception {
        String accessToken = authTestUtil.getTestToken(DEFAULT_LOGIN_DATA).accessToken();
        UpdateUserNameRequestDto updateData = new UpdateUserNameRequestDto(
                "123",
                "EMP003"
        );
        patch(URL)
                .key().auth(accessToken).body(updateData)
                .send().andExpect(status().isBadRequest())
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
        patch(URL)
                .key().auth("123dj3w989kp2ekohoiysofhawioerq87retreheiogujigbydfggauid")
                .body(updateData).send().andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value(ResponseCode.INVALID_TOKEN.getCode()))
                .andExpect(jsonPath("$.message").isNotEmpty());
    }

    @Test
    @DisplayName("아이디 변경 - 만료 된 토큰")
    void updateUserNameFailWithExpiredToken() throws Exception {
        String accessToken = authTestUtil.generateExpiredAccessToken(DEFAULT_LOGIN_DATA);
        UpdateUserNameRequestDto updateData = new UpdateUserNameRequestDto(
                "updateuser123",
                "EMP003"
        );
        patch(URL)
                .key().auth(accessToken).body(updateData)
                .send().andExpect(status().isUnauthorized())
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
        patch(URL)
                .key().body(updateData)
                .send().andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value(ResponseCode.UNAUTHORIZED.getCode()))
                .andExpect(jsonPath("$.message").isNotEmpty());
    }

    @Test
    @DisplayName("아이디 변경 - 비밀번호 오류")
    void updateUserNameFailWithPasswordError() throws Exception {
        String accessToken = authTestUtil.getTestToken(DEFAULT_LOGIN_DATA).accessToken();
        UpdateUserNameRequestDto updateData = new UpdateUserNameRequestDto(
                "updateuser123",
                "123"
        );
        patch(URL)
                .key().auth(accessToken).body(updateData)
                .send().andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value(ResponseCode.PASSWORD_ERROR.getCode()))
                .andExpect(jsonPath("$.message").isNotEmpty());
    }

    @Test
    @DisplayName("아이디 변경 - Api Key 오류")
    void updateUserNameFailWithKeyError() throws Exception {
        String accessToken = authTestUtil.getTestToken(DEFAULT_LOGIN_DATA).accessToken();
        UpdateUserNameRequestDto updateData = new UpdateUserNameRequestDto(
                "updateuser123",
                "EMP003"
        );
        patch(URL)
                .auth(accessToken).body(updateData)
                .send().andExpect(status().isForbidden())
                .andExpect(jsonPath("$.code").value(ResponseCode.KEY_ERROR.getCode()))
                .andExpect(jsonPath("$.message").isNotEmpty());
    }

    @Test
    @DisplayName("아이디 변경 - 아이디 중복")
    void updateUserNameFailWithDuplicateResource() throws Exception {
        String accessToken = authTestUtil.getTestToken(DEFAULT_LOGIN_DATA).accessToken();
        UpdateUserNameRequestDto updateData = new UpdateUserNameRequestDto(
                "EMP010",
                "EMP003"
        );
        patch(URL)
                .key().auth(accessToken).body(updateData)
                .send().andExpect(status().isConflict())
                .andExpect(jsonPath("$.code").value(ResponseCode.DUPLICATE_RESOURCE.getCode()))
                .andExpect(jsonPath("$.message").isNotEmpty());
    }
}
