package com.izen.module.employee.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "EmployeeCodeResponse")
public record EmployeeCodeResponseDto(
        @Schema(description = "사번", example = "EMP001")
        String employeeCode
) {
}
