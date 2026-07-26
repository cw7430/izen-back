package com.izen.module.auth;

import com.izen.common.api.doc.ErrorResponseDoc;
import com.izen.module.auth.dto.request.LoginRequestDto;
import com.izen.module.auth.dto.request.LogoutRequestDto;
import com.izen.module.auth.dto.request.RefreshRequestDto;
import com.izen.module.auth.dto.response.LoginResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
                    responseCode = "200", description = "로그인 성공", content = {
                    @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = LoginResponseDto.class)
                    )
            }
            ),
            @ApiResponse(
                    responseCode = "400", description = "입력값 오류", content = {
                    @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDoc.BadRequest.class)
                    )
            }
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
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto reqDto) {
        return ResponseEntity.ok(authService.login(reqDto));
    }

    @PostMapping("/refresh")
    @Operation(summary = "토큰 재발급")
    @SecurityRequirement(name = "refresh-token")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "재발급 성공", content = {
                    @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = LoginResponseDto.class)
                    )
            }
            ),
            @ApiResponse(
                    responseCode = "400", description = "입력값 오류", content = {
                    @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDoc.BadRequest.class)
                    )
            }
            ),
            @ApiResponse(
                    responseCode = "401", description = "인증오류", content = {
                    @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDoc.Unauthorized.class)
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
    public ResponseEntity<LoginResponseDto> refresh(HttpServletRequest req, @RequestBody RefreshRequestDto reqDto) {
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
    public ResponseEntity<Void> logout(@RequestBody LogoutRequestDto reqDto) {
        authService.logout(reqDto);
        return ResponseEntity.noContent().build();
    }
}
