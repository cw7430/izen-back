package com.izen.module.employee.dto.response;

import com.izen.module.employee.type.EmployeeRole;
import io.swagger.v3.oas.annotations.media.Schema;
import tools.jackson.databind.annotation.JsonSerialize;
import tools.jackson.databind.ser.std.ToStringSerializer;

import java.time.Instant;

@Schema(name = "EmployeeProfileResponse")
public record EmployeeProfileResponseDto(
        @Schema(description = "일련번호", example = "1", type = "string")
        @JsonSerialize(using = ToStringSerializer.class)
        Long employeeId,

        @Schema(description = "사번", example = "EMP001")
        String employeeCode,

        @Schema(description = "직급 권한", example = "EMPLOYEE")
        EmployeeRole employeeRole,

        @Schema(description = "사원 이름", example = "이사장")
        String employeeName,

        @Schema(description = "직급 코드", example = "PSN10")
        String positionCode,

        @Schema(description = "직급 이름", example = "대표")
        String positionName,

        @Schema(description = "부서 코드", example = "DPT100")
        String departmentCode,

        @Schema(description = "부서 이름", example = "경영지원부")
        String departmentName,

        @Schema(description = "팀 코드", example = "TM100")
        String teamCode,

        @Schema(description = "팀 이름", example = "경영팀")
        String teamName,

        @Schema(description = "휴대전화 번호", example = "010-1234-5678")
        String phone,

        @Schema(description = "이메일", example = "example@example.com")
        String email,

        @Schema(description = "작성자 일련번호", example = "1", type = "string", nullable = true)
        @JsonSerialize(using = ToStringSerializer.class)
        Long createdBy,

        @Schema(description = "작성자 이름", example = "이사장", nullable = true)
        String createdEmployeeName,

        @Schema(description = "수정자 일련번호", example = "1", type = "string", nullable = true)
        @JsonSerialize(using = ToStringSerializer.class)
        Long updatedBy,

        @Schema(description = "수정자 이름", example = "이사장", nullable = true)
        String updatedEmployeeName,

        @Schema(description = "입사일", example = "2006-03-02T12:30:44.461Z")
        Instant createdAt,

        @Schema(description = "수정일", example = "2006-03-02T12:30:44.461Z")
        Instant updatedAt,

        @Schema(description = "퇴사일", example = "null", nullable = true)
        Instant deletedAt
) {
}
