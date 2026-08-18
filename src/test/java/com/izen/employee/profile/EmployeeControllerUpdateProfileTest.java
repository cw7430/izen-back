package com.izen.employee.profile;

import com.izen.common.api.type.ResponseCode;
import com.izen.employee.EmployeeControllerTest;
import com.izen.module.employee.dto.request.UpdateEmployeeProfileRequestDto;
import com.izen.module.employee.type.EmployeeRole;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class EmployeeControllerUpdateProfileTest extends EmployeeControllerTest {
    private static final String URL = HR_URL + "/profiles/143";
    private static final String INVALID_URL = HR_URL + "/profiles/200";
    private static final UpdateEmployeeProfileRequestDto DATA = new UpdateEmployeeProfileRequestDto(
            "PSN70", "TM600", EmployeeRole.EMPLOYEE
    );
    private static final UpdateEmployeeProfileRequestDto INVALID_DATA = new UpdateEmployeeProfileRequestDto(
            "123", "456", EmployeeRole.EMPLOYEE
    );

    @Test
    @DisplayName("직원 프로필 수정 - 성공")
    void updateProfileSuccess() throws Exception {
        String accessToken = authTestUtil.getTestToken(PROFILE_LOGIN_DATA).accessToken();
        patch(URL)
                .key().auth(accessToken).body(DATA).send().andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("직원 프로필 수정 - 잘못 된 입력 값")
    void updateProfileFailWithValidationError() throws Exception {
        String accessToken = authTestUtil.getTestToken(PROFILE_LOGIN_DATA).accessToken();
        patch(URL)
                .key().auth(accessToken).body(INVALID_DATA).send().andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ResponseCode.VALIDATION_ERROR.getCode()))
                .andExpect(jsonPath("$.message").isNotEmpty());
    }

    @Test
    @DisplayName("직원 프로필 수정 - 잘못 된 토큰")
    void updateProfileFailWithInvalidToken() throws Exception {
        patch(URL)
                .key().auth(INVALID_TOKEN).body(DATA).send().andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value(ResponseCode.INVALID_TOKEN.getCode()))
                .andExpect(jsonPath("$.message").isNotEmpty());
    }

    @Test
    @DisplayName("직원 프로필 수정 - 만료 된 토큰")
    void updateProfileFailWithExpiredToken() throws Exception {
        String accessToken = authTestUtil.generateExpiredAccessToken(PROFILE_LOGIN_DATA);
        patch(URL)
                .key().auth(accessToken).body(DATA).send().andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value(ResponseCode.EXPIRED_TOKEN.getCode()))
                .andExpect(jsonPath("$.message").isNotEmpty());
    }

    @Test
    @DisplayName("직원 프로필 수정 - 인증 오류")
    void updateProfileFailWithUnauthorized() throws Exception {
        patch(URL)
                .key().body(DATA).send().andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value(ResponseCode.UNAUTHORIZED.getCode()))
                .andExpect(jsonPath("$.message").isNotEmpty());
    }

    @Test
    @DisplayName("직원 프로필 수정 - 권한 오류")
    void updateProfileFailWithForbidden() throws Exception {
        String accessToken = authTestUtil.getTestToken(SALARY_LOGIN_DATA).accessToken();
        patch(URL)
                .key().auth(accessToken).body(DATA).send().andExpect(status().isForbidden())
                .andExpect(jsonPath("$.code").value(ResponseCode.FORBIDDEN.getCode()))
                .andExpect(jsonPath("$.message").isNotEmpty());
    }

    @Test
    @DisplayName("직원 프로필 수정 - Api Key 오류")
    void updateProfileFailWithKeyError() throws Exception {
        String accessToken = authTestUtil.getTestToken(PROFILE_LOGIN_DATA).accessToken();
        patch(URL)
                .auth(accessToken).body(DATA).send().andExpect(status().isForbidden())
                .andExpect(jsonPath("$.code").value(ResponseCode.KEY_ERROR.getCode()))
                .andExpect(jsonPath("$.message").isNotEmpty());
    }

    @Test
    @DisplayName("직원 프로필 수정 - 요소 없음")
    void updateProfileFailWithResourceNotFound() throws Exception {
        String accessToken = authTestUtil.getTestToken(PROFILE_LOGIN_DATA).accessToken();
        patch(INVALID_URL)
                .key().auth(accessToken).body(DATA).send().andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(ResponseCode.RESOURCE_NOT_FOUND.getCode()))
                .andExpect(jsonPath("$.message").isNotEmpty());
    }
}
