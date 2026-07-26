package com.izen.module.auth.dto.vo;

import com.izen.module.auth.type.AuthRole;
import com.izen.module.employee.type.EmployeeRole;

public record LoginVo(
        Long accountId,
        String passwordHash,
        AuthRole authRole,
        String employeeCode,
        String employeeName,
        EmployeeRole employeeRole,
        String departmentCode,
        String teamCode,
        String positionCode
) {
}
