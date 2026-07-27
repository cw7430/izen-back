package com.izen.module.auth;

import com.izen.common.api.doc.ErrorResponseDoc;
import com.izen.module.auth.dto.request.*;
import com.izen.module.auth.dto.response.LoginResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Auth Controller", description = "계정 API")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "로그인")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "로그인 성공",
                    content =
                    @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = LoginResponseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "입력값 오류",
                    content =
                    @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDoc.BadRequest.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401", description = "잘못된 계정정보", content = {
                    @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDoc.LoginError.class)
                    )
            }
            ),
            @ApiResponse(
                    responseCode = "500", description = "서버 오류", content = {
                    @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDoc.InternalServerError.class)
                    )
            }
            )
    }
    )
    public ResponseEntity<LoginResponseDto> login(@RequestBody @Valid LoginRequestDto reqDto) {
        return ResponseEntity.ok(authService.login(reqDto));
    }

    @PostMapping("/refresh")
    @Operation(summary = "토큰 재발급")
    @SecurityRequirement(name = "refresh-token")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "재발급 성공",
                    content =
                    @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = LoginResponseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "입력값 오류",
                    content =
                    @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDoc.BadRequest.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "인증오류",
                    content =
                    @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDoc.Unauthorized.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 오류",
                    content =
                    @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDoc.InternalServerError.class)
                    )
            )
    }
    )
    public ResponseEntity<LoginResponseDto> refresh(HttpServletRequest req, @RequestBody @Valid RefreshRequestDto reqDto) {
        return ResponseEntity.ok(authService.refresh(req, reqDto));
    }

    @PostMapping("/logout")
    @Operation(summary = "로그아웃")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "로그아웃 성공"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 오류",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDoc.InternalServerError.class)
                    )
            )
    })
    public ResponseEntity<Void> logout(@RequestBody @Valid LogoutRequestDto reqDto) {
        authService.logout(reqDto);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/check-user")
    @Operation(summary = "아이디 중복 체크")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "중복 체크 성공"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "입력 값 오류",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDoc.BadRequest.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "아이디 중복",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDoc.DuplicateResource.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 오류",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDoc.InternalServerError.class)
                    )
            )
    })
    public ResponseEntity<Void> checkUser(@RequestBody @Valid CheckUserRequestDto reqDto) {
        authService.checkUserDuplicate(reqDto);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/user-name")
    @Operation(summary = "아이디 변경")
    @SecurityRequirement(name = "access-token")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "아이디 변경 성공"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "입력 값 오류",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDoc.BadRequest.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "인증 오류 (토큰 오류 또는 비밀번호 불일치)",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    oneOf = {
                                            ErrorResponseDoc.Unauthorized.class,
                                            ErrorResponseDoc.PasswordError.class
                                    }
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "아이디 중복",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDoc.DuplicateResource.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 오류",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDoc.InternalServerError.class)
                    )
            )
    })
    public ResponseEntity<Void> updateUserName(@RequestBody @Valid UpdateUserNameRequestDto reqDto) {
        authService.updateUserName(reqDto);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/password")
    @Operation(summary = "비밀번호 변경")
    @SecurityRequirement(name = "access-token")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "비밀번호 변경 성공"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "입력 값 오류",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDoc.BadRequest.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "인증 오류 (토큰 오류 또는 비밀번호 불일치)",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    oneOf = {
                                            ErrorResponseDoc.Unauthorized.class,
                                            ErrorResponseDoc.PasswordError.class
                                    }
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 오류",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDoc.InternalServerError.class)
                    )
            )
    })
    public ResponseEntity<Void> updatePassword(@RequestBody @Valid UpdatePasswordRequestDto reqDto) {
        authService.updatePassword(reqDto);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/account")
    @Operation(summary = "계정정보 변경")
    @SecurityRequirement(name = "access-token")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "계정정보 변경 성공"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "입력 값 오류",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDoc.BadRequest.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "인증 오류 (토큰 오류 또는 비밀번호 불일치)",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    oneOf = {
                                            ErrorResponseDoc.Unauthorized.class,
                                            ErrorResponseDoc.PasswordError.class
                                    }
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 오류",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDoc.InternalServerError.class)
                    )
            )
    })
    public ResponseEntity<Void> updateAccount(@RequestBody @Valid UpdateAccountRequestDto reqDto) {
        authService.updateAccount(reqDto);
        return ResponseEntity.noContent().build();
    }
}
