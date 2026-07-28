package com.izen.module.employee.type;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum EmployeeProfileSortPath {
    EMPLOYEE, POSITION, DEPARTMENT;

    @JsonValue
    public String getValue() {
        return name();
    }

    @JsonCreator
    public static EmployeeProfileSortPath from(String value) {
        if (value == null || value.isBlank()) {
            return EmployeeProfileSortPath.EMPLOYEE;
        }
        try {
            return EmployeeProfileSortPath.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            return EmployeeProfileSortPath.EMPLOYEE;
        }
    }
}
