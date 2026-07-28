package com.izen.module.employee;

import com.izen.common.api.doc.ErrorResponseDoc;
import com.izen.common.api.response.PageResponse;
import com.izen.module.employee.dto.request.EmployeeProfilesRequestDto;
import com.izen.module.employee.dto.response.EmployeeProfileResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/employee")
@RequiredArgsConstructor
@Tag(name = "Employee Controller", description = "인사 API")
public class EmployeeController {
    private final EmployeeService employeeService;

    @GetMapping("/profiles")
    @Operation(summary = "직원목록")
    @SecurityRequirement(name = "access-token")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "직원 목록 조회 성공",
                    content =
                    @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PageResponse.class)
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
                    description = "인증 오류",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    oneOf = {
                                            ErrorResponseDoc.Unauthorized.class,
                                            ErrorResponseDoc.ExpiredToken.class,
                                            ErrorResponseDoc.InvalidToken.class
                                    }
                            )
                    )
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
    public ResponseEntity<PageResponse<EmployeeProfileResponseDto>> getEmployeeProfileList(@ModelAttribute @Valid EmployeeProfilesRequestDto reqDto) {
        return ResponseEntity.ok(employeeService.getEmployeeProfileList(reqDto));
    }
}
