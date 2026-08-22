package com.izen.employee.profile;

import com.izen.common.api.type.ResponseCode;
import com.izen.employee.EmployeeControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class EmployeeControllerProfileListTest extends EmployeeControllerTest {
    private static final String URL = HR_URL + "/profiles";
    private static final String PARAM = "?page=1&size=5&blockSize=5&sortPath=employee&sortOrder=asc";
    private static final String INVALID_PARAM = "?page=0&size=0&blockSize=0&sortPath=0&sortOrder=0";
    private static final String INVALID_PAGE_PARAM = "?page=99999999&size=5&blockSize=5&sortPath=employee&sortOrder=asc";

    @Test
    @DisplayName("직원 프로필 목록 불러오기 - 성공")
    void getProfileListSuccess() throws Exception {
        String accessToken = authTestUtil.getTestToken(PROFILE_LOGIN_DATA).accessToken();
        get(URL + PARAM)
                .key().auth(accessToken).send().andExpect(status().isOk());
    }

    @Test
    @DisplayName("파라미터 없이 직원 프로필 목록 불러오기 - 성공")
    void getProfileListSuccessWithoutParam() throws Exception {
        String accessToken = authTestUtil.getTestToken(PROFILE_LOGIN_DATA).accessToken();
        get(URL)
                .key().auth(accessToken).send().andExpect(status().isOk());
    }

    @Test
    @DisplayName("직원 프로필 목록 불러오기 - 잘 못된 파라미터")
    void getProfileListFailWithValidationError() throws Exception {
        String accessToken = authTestUtil.getTestToken(PROFILE_LOGIN_DATA).accessToken();
        get(URL + INVALID_PARAM)
                .key().auth(accessToken).send().andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ResponseCode.VALIDATION_ERROR.getCode()))
                .andExpect(jsonPath("$.message").isNotEmpty());
    }

    @Test
    @DisplayName("직원 프로필 목록 불러오기 - 잘 못된 토큰")
    void getProfileListFailWithInvalidToken() throws Exception {
        get(URL + PARAM)
                .key().auth(INVALID_TOKEN).send().andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value(ResponseCode.INVALID_TOKEN.getCode()))
                .andExpect(jsonPath("$.message").isNotEmpty());
    }

    @Test
    @DisplayName("직원 프로필 목록 불러오기 - 만료된 토큰")
    void getProfileListFailWithExpiredToken() throws Exception {
        String accessToken = authTestUtil.generateExpiredAccessToken(PROFILE_LOGIN_DATA);
        get(URL + PARAM)
                .key().auth(accessToken).send().andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value(ResponseCode.EXPIRED_TOKEN.getCode()))
                .andExpect(jsonPath("$.message").isNotEmpty());
    }

    @Test
    @DisplayName("직원 프로필 목록 불러오기 - 인증 오류")
    void getProfileListFailWithUnauthorized() throws Exception {
        get(URL + PARAM)
                .key().send().andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value(ResponseCode.UNAUTHORIZED.getCode()))
                .andExpect(jsonPath("$.message").isNotEmpty());
    }

    @Test
    @DisplayName("직원 프로필 목록 불러오기 - Api Key 오류")
    void getProfileListFailWithKeyError() throws Exception {
        String accessToken = authTestUtil.getTestToken(PROFILE_LOGIN_DATA).accessToken();
        get(URL + PARAM)
                .auth(accessToken).send().andExpect(status().isForbidden())
                .andExpect(jsonPath("$.code").value(ResponseCode.KEY_ERROR.getCode()))
                .andExpect(jsonPath("$.message").isNotEmpty());
    }

    @Test
    @DisplayName("직원 프로필 목록 불러오기 - 요소 없음")
    void getProfileListFailWithResourceNotFound() throws Exception {
        String accessToken = authTestUtil.getTestToken(PROFILE_LOGIN_DATA).accessToken();
        get(URL + INVALID_PAGE_PARAM)
                .key().auth(accessToken).send().andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(ResponseCode.RESOURCE_NOT_FOUND.getCode()))
                .andExpect(jsonPath("$.message").isNotEmpty());
    }
}
