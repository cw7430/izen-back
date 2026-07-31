package com.izen.module.employee.dto.response;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tools.jackson.databind.annotation.JsonSerialize;
import tools.jackson.databind.ser.std.ToStringSerializer;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "DepartmentResponse")
public class DepartmentResponseDto {
    @Schema(description = "일련번호", example = "1", type = "string")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long departmentId;

    @Schema(description = "부서 코드", example = "DPT100")
    private String departmentCode;

    @Schema(description = "부서 이름", example = "경영지원부")
    private String departmentName;

    @ArraySchema(schema = @Schema(implementation = TeamResponseDto.class))
    private List<TeamResponseDto> teams;
}
