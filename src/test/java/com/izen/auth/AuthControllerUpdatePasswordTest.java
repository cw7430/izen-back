package com.izen.auth;

import com.izen.BaseIntegrationTest;
import com.izen.module.auth.dto.request.LoginRequestDto;
import com.izen.module.auth.dto.request.UpdatePasswordRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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
}
