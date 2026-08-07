package com.izen.module.employee.dto.response;

import com.izen.module.employee.dto.vo.EmployeeProfileVo;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeProfileResponseDto extends EmployeeProfileVo {
    @ArraySchema(schema = @Schema(implementation = DepartmentResponseDto.class))
    private List<DepartmentResponseDto> departments;

    @ArraySchema(schema = @Schema(implementation = PositionResponseDto.class))
    private List<PositionResponseDto> positions;

    @Schema(description = "허용된 팀", example = "[\"TM100\", \"TM200\"]")
    private Set<String> allowedProfileTeams;
}
