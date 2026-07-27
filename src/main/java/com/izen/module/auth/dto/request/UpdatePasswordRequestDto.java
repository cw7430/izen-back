package com.izen.module.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Schema(name = "UpdatePasswordRequest")
public record UpdatePasswordRequestDto(
        @NotBlank(message = "기존 비밀번호를 입력해주세요.")
        @Schema(description = "기존 비빌번호", example = "examplepw1234!@")
        String prevPassword,

        @NotBlank(message = "새 비밀번호를 입력해주세요.")
        @Pattern(
                regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-={}\\[\\]|:;\"'<>,.?/~`]).{10,25}$",
                message = "비밀번호는 10자 이상 25자 이하이며, 영문, 숫자, 특수문자를 각각 1개 이상 포함해야 합니다."
        )
        @Schema(description = "새 비빌번호", example = "examplepw4321!@")
        String newPassword
) {
}
