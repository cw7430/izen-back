package com.izen.module.employee.type;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.izen.common.api.exception.CustomException;
import com.izen.common.api.type.ResponseCode;

public enum EmployeeRole {
    DEPARTMENT_CHIEF, TEAM_CHIEF, EMPLOYEE, LEFT;

    @JsonValue
    public String getValue() {
        return name();
    }

    @JsonCreator
    public static EmployeeRole from(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            return EmployeeRole.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new CustomException(
                    ResponseCode.VALIDATION_ERROR,
                    "employeeRole",
                    "입력 가능값: DEPARTMENT_CHIEF, TEAM_CHIEF, EMPLOYEE, LEFT, 입력된 값: " + value
            );
        }
    }
}
