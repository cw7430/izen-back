package com.izen.employee;

import com.izen.BaseIntegrationTest;
import com.izen.module.auth.dto.request.LoginRequestDto;

public class EmployeeControllerTest extends BaseIntegrationTest {
    protected static final String HR_URL = BASE_URL + "/hr";
    protected static final LoginRequestDto PROFILE_LOGIN_DATA = new LoginRequestDto(
            "EMP003",
            "EMP003",
            false
    );
    protected static final LoginRequestDto SALARY_LOGIN_DATA = new LoginRequestDto(
            "EMP019",
            "EMP019",
            false
    );
}
