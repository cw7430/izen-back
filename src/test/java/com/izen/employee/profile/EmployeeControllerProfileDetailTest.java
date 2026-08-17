package com.izen.employee.profile;

import com.izen.common.api.type.ResponseCode;
import com.izen.employee.EmployeeControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class EmployeeControllerProfileDetailTest extends EmployeeControllerTest {
    private static final String URL = HR_URL + "/profiles/1";
    private static final String INVALID_URL = HR_URL + "/profiles/0";

    @Test
    @DisplayName("직원 상세 프로필 불러오기 - 성공")
    void getProfileDetailSuccess() throws Exception {
        String accessToken = authTestUtil.getTestToken(PROFILE_LOGIN_DATA).accessToken();
        get(URL)
                .key().auth(accessToken).send().andExpect(status().isOk());
    }

    @Test
    @DisplayName("직원 상세 프로필 불러오기 - 잘못된 토큰")
    void getProfileDetailFailWithInvalidToken() throws Exception {
        get(URL)
                .key().auth(INVALID_TOKEN).send().andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value(ResponseCode.INVALID_TOKEN.getCode()))
                .andExpect(jsonPath("$.message").isNotEmpty());
    }

    @Test
    @DisplayName("직원 상세 프로필 불러오기 - 만료된 토큰")
    void getProfileDetailFailWithExpiredToken() throws Exception {
        String accessToken = authTestUtil.generateExpiredAccessToken(PROFILE_LOGIN_DATA);
        get(URL)
                .key().auth(accessToken).send().andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value(ResponseCode.EXPIRED_TOKEN.getCode()))
                .andExpect(jsonPath("$.message").isNotEmpty());
    }

    @Test
    @DisplayName("직원 상세 프로필 불러오기 - 인증 오류")
    void getProfileDetailFailWithUnauthorized() throws Exception {
        get(URL)
                .key().send().andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value(ResponseCode.UNAUTHORIZED.getCode()))
                .andExpect(jsonPath("$.message").isNotEmpty());
    }

    @Test
    @DisplayName("직원 상세 프로필 불러오기 - Api Key 오류")
    void getProfileDetailFailWithKeyError() throws Exception {
        String accessToken = authTestUtil.getTestToken(PROFILE_LOGIN_DATA).accessToken();
        get(URL)
                .auth(accessToken).send().andExpect(status().isForbidden())
                .andExpect(jsonPath("$.code").value(ResponseCode.KEY_ERROR.getCode()))
                .andExpect(jsonPath("$.message").isNotEmpty());
    }

    @Test
    @DisplayName("직원 상세 프로필 불러오기 - 요소 없음")
    void getProfileDetailFailWithResourceNotFound() throws Exception {
        String accessToken = authTestUtil.getTestToken(PROFILE_LOGIN_DATA).accessToken();
        get(INVALID_URL)
                .key().auth(accessToken).send().andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(ResponseCode.RESOURCE_NOT_FOUND.getCode()))
                .andExpect(jsonPath("$.message").isNotEmpty());
    }
}
