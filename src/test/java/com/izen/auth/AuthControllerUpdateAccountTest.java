package com.izen.auth;

import com.izen.module.auth.dto.request.UpdateAccountRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthControllerUpdateAccountTest extends AuthControllerTest {
    private static final String URL = AUTH_URL + "/account";

    @Test
    @DisplayName("계정정보 변경 - 성공")
    void updateAccountSuccess() throws Exception {
        String accessToken = authTestUtil.getTestToken(DEFAULT_LOGIN_DATA).accessToken();
        UpdateAccountRequestDto updateData = new UpdateAccountRequestDto(
                "EMP003",
                "010-0000-0000",
                "email@email.com"
        );
        patch(URL)
                .key().auth(accessToken).body(updateData)
                .send().andExpect(status().isNoContent());
    }
}
