package com.izen.module.employee.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tools.jackson.databind.annotation.JsonSerialize;
import tools.jackson.databind.ser.std.ToStringSerializer;

@Schema(name = "PositionResponse")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PositionResponseDto {
    @Schema(description = "일련번호", example = "1", type = "string")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long positionId;

    @Schema(description = "직급 코드", example = "PSN10")
    private String positionCode;

    @Schema(description = "직급 이름", example = "대표")
    private String positionName;

    @Schema(description = "기본급", example = "1000000", type = "string")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long basicSalary;

    @Schema(description = "성과급", example = "100000", type = "string")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long incentiveSalary;
}