package com.izen.module.employee.dto.request;

import com.izen.module.employee.type.EmployeeRole;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record UpdateEmployeeProfileRequestDto(
        @NotBlank(message = "직급을 선택해주세요.")
        @Pattern(
                regexp = "^PSN\\d{2,3}$",
                message = "직급 코드 형식이 올바르지 않습니다."
        )
        @Schema(description = "직급 코드", example = "PSN10")
        String positionCode,

        @NotBlank(message = "팀을 선택해주세요.")
        @Pattern(
                regexp = "^TM\\d{3}$",
                message = "팀 코드 형식이 올바르지 않습니다."
        )
        @Schema(description = "팀 코드", example = "TM100")
        String teamCode,

        @NotBlank(message = "직책을 선택해주세요.")
        @Schema(description = "직책 권한", example = "EMPLOYEE")
        EmployeeRole employeeRole
) {
}
