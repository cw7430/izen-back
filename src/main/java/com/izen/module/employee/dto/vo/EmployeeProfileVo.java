package com.izen.module.employee.dto.vo;

import com.izen.module.employee.type.EmployeeRole;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tools.jackson.databind.annotation.JsonSerialize;
import tools.jackson.databind.ser.std.ToStringSerializer;

import java.time.Instant;

@Schema(name = "EmployeeProfileResponse")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeProfileVo {
    @Schema(description = "일련번호", example = "1", type = "string")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long employeeId;

    @Schema(description = "사번", example = "EMP001")
    private String employeeCode;

    @Schema(description = "직책 권한", example = "EMPLOYEE")
    private EmployeeRole employeeRole;

    @Schema(description = "사원 이름", example = "이사장")
    private String employeeName;

    @Schema(description = "직급 코드", example = "PSN10")
    private String positionCode;

    @Schema(description = "직급 이름", example = "대표")
    private String positionName;

    @Schema(description = "부서 코드", example = "DPT100")
    private String departmentCode;

    @Schema(description = "부서 이름", example = "경영지원부")
    private String departmentName;

    @Schema(description = "팀 코드", example = "TM100")
    private String teamCode;

    @Schema(description = "팀 이름", example = "경영팀")
    private String teamName;

    @Schema(description = "휴대전화 번호", example = "010-1234-5678")
    private String phone;

    @Schema(description = "이메일", example = "example@example.com")
    private String email;

    @Schema(description = "작성자 일련번호", example = "1", type = "string", nullable = true)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long createdBy;

    @Schema(description = "작성자 이름", example = "이사장", nullable = true)
    private String createdEmployeeName;

    @Schema(description = "수정자 일련번호", example = "1", type = "string", nullable = true)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long updatedBy;

    @Schema(description = "수정자 이름", example = "이사장", nullable = true)
    private String updatedEmployeeName;

    @Schema(description = "입사일", example = "2006-03-02T12:30:44.461Z")
    private Instant createdAt;

    @Schema(description = "수정일", example = "2006-03-02T12:30:44.461Z")
    private Instant updatedAt;

    @Schema(description = "퇴사일", example = "null", nullable = true)
    private Instant deletedAt;
}
