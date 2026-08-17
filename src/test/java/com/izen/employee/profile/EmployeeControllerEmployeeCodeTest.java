package com.izen.employee.profile;

import com.izen.common.api.type.ResponseCode;
import com.izen.employee.EmployeeControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class EmployeeControllerEmployeeCodeTest extends EmployeeControllerTest {
    private static final String URL = HR_URL + "/profiles/employee-code";

    @Test
    @DisplayName("사번 생성 - 성공")
    void getEmployeeCodeSuccess() throws Exception {
        String accessToken = authTestUtil.getTestToken(PROFILE_LOGIN_DATA).accessToken();
        get(URL)
                .key().auth(accessToken).send().andExpect(status().isOk());
    }

    @Test
    @DisplayName("사번 생성 - 잘못된 토큰")
    void getEmployeeCodeFailWithInvalidToken() throws Exception {
        get(URL)
                .key().auth(INVALID_TOKEN).send().andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value(ResponseCode.INVALID_TOKEN.getCode()))
                .andExpect(jsonPath("$.message").isNotEmpty());
    }

    @Test
    @DisplayName("사번 생성 - 만료된 토큰")
    void getEmployeeCodeFailWithExpiredToken() throws Exception {
        String accessToken = authTestUtil.generateExpiredAccessToken(PROFILE_LOGIN_DATA);
        get(URL)
                .key().auth(accessToken).send().andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value(ResponseCode.EXPIRED_TOKEN.getCode()))
                .andExpect(jsonPath("$.message").isNotEmpty());
    }

    @Test
    @DisplayName("사번 생성 - 인증 오류")
    void getEmployeeCodeFailWithUnauthorized() throws Exception {
        get(URL)
                .key().send().andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value(ResponseCode.UNAUTHORIZED.getCode()))
                .andExpect(jsonPath("$.message").isNotEmpty());
    }

    @Test
    @DisplayName("사번 생성 - 권한 오류")
    void getEmployeeCodeFailWithForbidden() throws Exception {
        String accessToken = authTestUtil.getTestToken(SALARY_LOGIN_DATA).accessToken();
        get(URL)
                .key().auth(accessToken).send().andExpect(status().isForbidden())
                .andExpect(jsonPath("$.code").value(ResponseCode.FORBIDDEN.getCode()))
                .andExpect(jsonPath("$.message").isNotEmpty());
    }

    @Test
    @DisplayName("사번 생성 - Api Key 오류")
    void getEmployeeCodeFailWithKeyError() throws Exception {
        String accessToken = authTestUtil.getTestToken(PROFILE_LOGIN_DATA).accessToken();
        get(URL)
                .auth(accessToken).send().andExpect(status().isForbidden())
                .andExpect(jsonPath("$.code").value(ResponseCode.KEY_ERROR.getCode()))
                .andExpect(jsonPath("$.message").isNotEmpty());
    }
}
