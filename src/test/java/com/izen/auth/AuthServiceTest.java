package com.izen.auth;

import com.izen.BaseIntegrationTest;
import com.izen.common.api.exception.CustomException;
import com.izen.module.auth.AuthService;
import com.izen.module.auth.dto.request.LoginRequestDto;
import com.izen.module.auth.dto.response.LoginResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
public class AuthServiceTest extends BaseIntegrationTest {
    @Autowired
    private AuthService authService;

    @Test
    @DisplayName("로그인 성공")
    public void loginSuccess() {
        LoginRequestDto data = new LoginRequestDto(
                "EMP003",
                "EMP003",
                false
        );
        LoginResponseDto resDto = authService.login(data);

        assertThat(resDto.accessToken()).isNotBlank();
        assertThat(resDto.refreshToken()).isNotBlank();
    }

    @Test
    @DisplayName("잘못 된 아이디 또는 비밀번호")
    public void loginFailWithLoginError() {
        LoginRequestDto data = new LoginRequestDto(
                "EMP002",
                "EMP002",
                true
        );
        assertThatThrownBy(() -> authService.login(data))
                .isInstanceOf(CustomException.class);
    }
}
