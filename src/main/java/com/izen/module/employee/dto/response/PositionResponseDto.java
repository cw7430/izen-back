package com.izen.module.employee.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import tools.jackson.databind.annotation.JsonSerialize;
import tools.jackson.databind.ser.std.ToStringSerializer;

@Schema(name = "PositionResponse")
public record PositionResponseDto(
        @Schema(description = "일련번호", example = "1", type = "string")
        @JsonSerialize(using = ToStringSerializer.class)
        Long positionId,

        @Schema(description = "직급 코드", example = "PSN10")
        String positionCode,

        @Schema(description = "직급 이름", example = "대표")
        String positionName,

        @Schema(description = "기본급", example = "1000000", type = "string")
        @JsonSerialize(using = ToStringSerializer.class)
        Long basicSalary,

        @Schema(description = "성과급", example = "100000", type = "string")
        @JsonSerialize(using = ToStringSerializer.class)
        Long incentiveSalary
) {
}
