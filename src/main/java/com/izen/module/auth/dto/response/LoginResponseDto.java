package com.izen.module.auth.dto.response;

import com.izen.module.auth.type.AuthRole;
import com.izen.module.employee.type.EmployeeRole;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "LoginResponse")
public record LoginResponseDto(
        @Schema(description = "access token")
        String accessToken,
        @Schema(description = "access token 만료시간", example = "1773042557262")
        long accessTokenExpiresAtMs,
        @Schema(description = "refresh token")
        String refreshToken,
        @Schema(description = "refresh token 만료시간", example = "1773042557262")
        long refreshTokenExpiresAtMs,
        @Schema(description = "장기 갱신", example = "false")
        boolean isAuto,
        @Schema(description = "사번", example = "EMP001")
        String employeeCode,
        @Schema(description = "사원 이름", example = "이사장")
        String employeeName,
        @Schema(description = "계정 권한", example = "USER")
        AuthRole authRole,
        @Schema(description = "직급 권한", example = "EMPLOYEE")
        EmployeeRole employeeRole,
        @Schema(description = "부서 코드", example = "DPT100")
        String department,
        @Schema(description = "팀 코드", example = "TM100")
        String team,
        @Schema(description = "직급 코드", example = "PSN10")
        String position
) {
}
