package com.izen.employee.profile;

import com.izen.common.api.type.ResponseCode;
import com.izen.employee.EmployeeControllerTest;
import com.izen.module.employee.dto.request.CreateEmployeeProfileRequestDto;
import com.izen.module.employee.type.EmployeeRole;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class EmployeeControllerCreateProfileTest extends EmployeeControllerTest {
    private static final String URL = HR_URL + "/profiles";
    private static final CreateEmployeeProfileRequestDto DATA = new CreateEmployeeProfileRequestDto(
            "EMP10000", "김사원", "010-0000-0000",
            "email@email.com", "PSN80", "TM601",
            EmployeeRole.EMPLOYEE
    );
    private static final CreateEmployeeProfileRequestDto INVALID_DATA = new CreateEmployeeProfileRequestDto(
            "123", "456", "789",
            "101112", "131415", "161719",
            EmployeeRole.EMPLOYEE
    );

    @Test
    @DisplayName("직원 프로필 등록 - 성공")
    void createProfileSuccess() throws Exception {
        String accessToken = authTestUtil.getTestToken(PROFILE_LOGIN_DATA).accessToken();
        post(URL)
                .key().auth(accessToken).body(DATA).send().andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("직원 프로필 등록 - 잘못 된 입력 값")
    void createProfileFailWithValidationError() throws Exception {
        String accessToken = authTestUtil.getTestToken(PROFILE_LOGIN_DATA).accessToken();
        post(URL)
                .key().auth(accessToken).body(INVALID_DATA).send().andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ResponseCode.VALIDATION_ERROR.getCode()))
                .andExpect(jsonPath("$.message").isNotEmpty());
    }

    @Test
    @DisplayName("직원 프로필 등록 - 잘못 된 토큰")
    void createProfileFailWithInvalidToken() throws Exception {
        post(URL)
                .key().auth(INVALID_TOKEN).body(DATA).send().andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value(ResponseCode.INVALID_TOKEN.getCode()))
                .andExpect(jsonPath("$.message").isNotEmpty());
    }

    @Test
    @DisplayName("직원 프로필 등록 - 만료 된 토큰")
    void createProfileFailWithExpiredToken() throws Exception {
        String accessToken = authTestUtil.generateExpiredAccessToken(PROFILE_LOGIN_DATA);
        post(URL)
                .key().auth(accessToken).body(DATA).send().andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value(ResponseCode.EXPIRED_TOKEN.getCode()))
                .andExpect(jsonPath("$.message").isNotEmpty());
    }

    @Test
    @DisplayName("직원 프로필 등록 - 인증 오류")
    void createProfileFailWithUnauthorized() throws Exception {
        post(URL)
                .key().body(DATA).send().andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value(ResponseCode.UNAUTHORIZED.getCode()))
                .andExpect(jsonPath("$.message").isNotEmpty());
    }

    @Test
    @DisplayName("직원 프로필 등록 - 권한 오류")
    void createProfileFailWithForbidden() throws Exception {
        String accessToken = authTestUtil.getTestToken(SALARY_LOGIN_DATA).accessToken();
        post(URL)
                .key().auth(accessToken).body(DATA).send().andExpect(status().isForbidden())
                .andExpect(jsonPath("$.code").value(ResponseCode.FORBIDDEN.getCode()))
                .andExpect(jsonPath("$.message").isNotEmpty());
    }

    @Test
    @DisplayName("직원 프로필 등록 - Api Key 오류")
    void createProfileFailWithKeyError() throws Exception {
        String accessToken = authTestUtil.getTestToken(PROFILE_LOGIN_DATA).accessToken();
        post(URL)
                .auth(accessToken).body(DATA).send().andExpect(status().isForbidden())
                .andExpect(jsonPath("$.code").value(ResponseCode.KEY_ERROR.getCode()))
                .andExpect(jsonPath("$.message").isNotEmpty());
    }
}
