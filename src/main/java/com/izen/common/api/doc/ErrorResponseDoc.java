package com.izen.common.api.doc;

import com.izen.common.api.type.ResponseCode;
import com.izen.common.api.type.ValidationError;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.List;

@Schema(name = "ErrorResponse")
public class ErrorResponseDoc {
    /**
     * 400 Bad Request - Validation Error
     */
    @Getter
    public static class BadRequest {
        @Schema(example = "VE")
        private final String code = ResponseCode.VALIDATION_ERROR.getCode();

        @Schema(example = "입력값이 잘못되었습니다.")
        private final String message = ResponseCode.VALIDATION_ERROR.getMessage();

        @Schema(description = "에러내용")
        private final List<ValidationError> validationErrors = null;
    }

    /**
     * 401 Unauthorized - Unauthorized
     */
    @Getter
    public static class Unauthorized {
        @Schema(example = "UA")
        private final String code = ResponseCode.UNAUTHORIZED.getCode();

        @Schema(example = "로그인이 필요합니다.")
        private final String message = ResponseCode.UNAUTHORIZED.getMessage();
    }

    /**
     * 401 Unauthorized - Login Error
     */
    @Getter
    public static class LoginError {
        @Schema(example = "LGE")
        private final String code = ResponseCode.LOGIN_ERROR.getCode();

        @Schema(example = "아이디 또는 비밀번호가 잘못되었습니다.")
        private final String message = ResponseCode.LOGIN_ERROR.getMessage();
    }

    /**
     * 401 Unauthorized - Password Error
     */
    @Getter
    public static class PasswordError {
        @Schema(example = "PWE")
        private final String code = ResponseCode.PASSWORD_ERROR.getCode();

        @Schema(example = "비밀번호가 잘못되었습니다.")
        private final String message = ResponseCode.PASSWORD_ERROR.getMessage();
    }

    /**
     * 401 Unauthorized - Expired Token
     */
    @Getter
    public static class ExpiredToken {
        @Schema(example = "ET")
        private final String code = ResponseCode.EXPIRED_TOKEN.getCode();

        @Schema(example = "토큰이 만료되었습니다.")
        private final String message = ResponseCode.EXPIRED_TOKEN.getMessage();
    }

    /**
     * 401 Unauthorized - Invalid Token
     */
    @Getter
    public static class InvalidToken {
        @Schema(example = "IT")
        private final String code = ResponseCode.INVALID_TOKEN.getCode();

        @Schema(example = "유효하지 않은 토큰입니다.")
        private final String message = ResponseCode.INVALID_TOKEN.getMessage();
    }

    /**
     * 403 Forbidden - Forbidden
     */
    @Getter
    public static class Forbidden {
        @Schema(example = "FB")
        private final String code = ResponseCode.FORBIDDEN.getCode();

        @Schema(example = "접근 권한이 없습니다.")
        private final String message = ResponseCode.FORBIDDEN.getMessage();
    }

    /**
     * 404 Not Found - Resource Not Found
     */
    @Getter
    public static class ResourceNotFound {
        @Schema(example = "RNF")
        private final String code = ResponseCode.RESOURCE_NOT_FOUND.getCode();

        @Schema(example = "요청한 자원을 찾을 수 없습니다.")
        private final String message = ResponseCode.RESOURCE_NOT_FOUND.getMessage();
    }

    /**
     * 404 Not Found - Endpoint Not Found
     */
    @Getter
    public static class EndpointNotFound {
        @Schema(example = "ENF")
        private final String code = ResponseCode.ENDPOINT_NOT_FOUND.getCode();

        @Schema(example = "요청한 경로가 잘못되었습니다.")
        private final String message = ResponseCode.ENDPOINT_NOT_FOUND.getMessage();
    }

    /**
     * 409 Conflict - Duplicate Resource
     */
    @Getter
    public static class DuplicateResource {
        @Schema(example = "DR")
        private final String code = ResponseCode.DUPLICATE_RESOURCE.getCode();

        @Schema(example = "이미 존재하는 항목입니다.")
        private final String message = ResponseCode.DUPLICATE_RESOURCE.getMessage();
    }

    /**
     * 409 Conflict - Conflict
     */
    @Getter
    public static class Conflict {
        @Schema(example = "CF")
        private final String code = ResponseCode.CONFLICT.getCode();

        @Schema(example = "요청이 현재 상태와 충돌합니다.")
        private final String message = ResponseCode.CONFLICT.getMessage();
    }

    /**
     * 500 Internal Server Error
     */
    @Getter
    public static class InternalServerError {
        @Schema(example = "ISE")
        private final String code = ResponseCode.INTERNAL_SERVER_ERROR.getCode();

        @Schema(example = "서버에서 문제가 발생했습니다.")
        private final String message = ResponseCode.INTERNAL_SERVER_ERROR.getMessage();
    }
}