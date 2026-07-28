package com.izen.module.employee.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import tools.jackson.databind.annotation.JsonSerialize;
import tools.jackson.databind.ser.std.ToStringSerializer;

@Getter
@AllArgsConstructor
@Schema(name = "TeamResponse")
public class TeamResponseDto {
    @Schema(description = "일련번호", example = "1", type = "string")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long teamId;

    @Schema(description = "팀 코드", example = "TM100")
    private String teamCode;

    @Schema(description = "팀 이름", example = "경영팀")
    private String teamName;
}
