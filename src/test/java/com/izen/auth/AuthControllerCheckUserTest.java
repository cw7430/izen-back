package com.izen.auth;


import com.izen.common.api.type.ResponseCode;
import com.izen.module.auth.dto.request.CheckUserRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthControllerCheckUserTest extends AuthControllerTest {

    private static final String URL = AUTH_URL + "/check-user";
    private static final CheckUserRequestDto DATA = new CheckUserRequestDto("user1234");
    private static final CheckUserRequestDto INVALID_DATA = new CheckUserRequestDto("123");
    private static final CheckUserRequestDto DUPLICATE_DATA = new CheckUserRequestDto("EMP003");

    @Test
    @DisplayName("아이디 중복체크 - 성공")
    void checkUserSuccess() throws Exception {
        post(URL)
                .key().body(DATA).send()
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("아이디 중복체크 - 잘 못된 입력 값")
    void checkUserFailWithValidationError() throws Exception {
        post(URL)
                .key().body(INVALID_DATA).send()
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ResponseCode.VALIDATION_ERROR.getCode()))
                .andExpect(jsonPath("$.message").isNotEmpty());
    }

    @Test
    @DisplayName("아이디 중복체크 - Api Key 오류")
    void checkUserFailWithKeyError() throws Exception {
        post(URL)
                .body(DATA).send()
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.code").value(ResponseCode.KEY_ERROR.getCode()))
                .andExpect(jsonPath("$.message").isNotEmpty());
    }

    @Test
    @DisplayName("아이디 중복체크 - 아이디 중복")
    void checkUserFailWithDuplicateResource() throws Exception {
        post(URL)
                .key().body(DUPLICATE_DATA).send()
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code").value(ResponseCode.DUPLICATE_RESOURCE.getCode()))
                .andExpect(jsonPath("$.message").isNotEmpty());
    }
}
