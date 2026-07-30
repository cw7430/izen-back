package com.izen.module.employee.dto.response;

import com.izen.module.employee.type.EmployeeRole;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.Instant;
import java.util.List;

@Schema(name = "UpsertProfileInfoResponse")
@Getter
public class UpsertProfileInfoResponseDto extends EmployeeProfileResponseDto {

    public UpsertProfileInfoResponseDto(
            Long employeeId,
            String employeeCode,
            EmployeeRole employeeRole,
            String employeeName,
            String positionCode,
            String positionName,
            String departmentCode,
            String departmentName,
            String teamCode,
            String teamName,
            String phone,
            String email,
            Long createdBy,
            String createdEmployeeName,
            Long updatedBy,
            String updatedEmployeeName,
            Instant createdAt,
            Instant updatedAt,
            Instant deletedAt,
            List<DepartmentResponseDto> departments,
            List<PositionResponseDto> positions
    ) {
        super(
                employeeId,
                employeeCode,
                employeeRole,
                employeeName,
                positionCode,
                positionName,
                departmentCode,
                departmentName,
                teamCode,
                teamName,
                phone,
                email,
                createdBy,
                createdEmployeeName,
                updatedBy,
                updatedEmployeeName,
                createdAt,
                updatedAt,
                deletedAt
        );
        this.departments = departments;
        this.positions = positions;
    }

    @ArraySchema(schema = @Schema(implementation = DepartmentResponseDto.class))
    private final List<DepartmentResponseDto> departments;

    @ArraySchema(schema = @Schema(implementation = PositionResponseDto.class))
    private final List<PositionResponseDto> positions;
}
