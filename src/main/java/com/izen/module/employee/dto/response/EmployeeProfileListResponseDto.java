package com.izen.module.employee.dto.response;

import com.izen.common.api.response.PageResponse;
import com.izen.module.employee.dto.vo.EmployeeProfileVo;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.Set;

@Schema(name = "EmployeeProfileListResponse")
@Getter
@AllArgsConstructor
public class EmployeeProfileListResponseDto {
    PageResponse<EmployeeProfileVo> employeeProfiles;

    @ArraySchema(schema = @Schema(implementation = DepartmentResponseDto.class))
    private List<DepartmentResponseDto> departments;

    @ArraySchema(schema = @Schema(implementation = PositionResponseDto.class))
    private List<PositionResponseDto> positions;

    @Schema(description = "허용된 팀", example = "[\"TM100\", \"TM200\"]")
    private Set<String> allowedProfileTeams;
}
