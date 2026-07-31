package com.izen.module.employee.dto.request;

import com.izen.module.employee.type.EmployeeRole;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Schema(name = "CreateEmployeeProfile")
public record CreateEmployeeProfileRequestDto(
        @NotBlank(message = "사번을 생성해주세요.")
        @Pattern(
                regexp = "^EMP\\d{3,}$",
                message = "사번 형식이 올바르지 않습니다."
        )
        @Schema(description = "사번", example = "EMP001")
        String employeeCode,

        @NotBlank(message = "사원 이름을 입력해주세요.")
        @Schema(description = "사원 이름", example = "이사장")
        String employeeName,

        @NotBlank(message = "전화번호를 입력해주세요.")
        @Pattern(
                regexp = "^(010|011|016|017|018|019)-\\d{3,4}-\\d{4}$",
                message = "전화번호 형식이 올바르지 않습니다."
        )
        @Schema(description = "휴대전화 번호", example = "010-1234-5678")
        String phone,

        @NotBlank(message = "이메일을 입력해주세요.")
        @Email(message = "이메일 형식이 올바르지 않습니다.")
        @Schema(description = "이메일", example = "example@example.com")
        String email,

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
