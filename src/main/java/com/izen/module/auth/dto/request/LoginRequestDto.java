package com.izen.module.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(name = "LoginRequest")
public record LoginRequestDto(
        @NotBlank(message = "아이디를 입력해주세요.")
        @Schema(description = "아이디", example = "exampleid1234")
        String userName,

        @NotBlank(message = "비밀번호를 입력해주세요.")
        @Schema(description = "비빌번호", example = "examplepw1234!@")
        String password,

        @Schema(description = "장기 로그인", example = "false")
        boolean isAuto
) {
}
