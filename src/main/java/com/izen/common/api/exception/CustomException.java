package com.izen.common.api.exception;

import com.izen.common.api.type.ResponseCode;
import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {
    private final ResponseCode responseCode;
    private final String fieldName;
    private final String customMessage;

    public CustomException(ResponseCode responseCode) {
        super(responseCode.getMessage());
        this.responseCode = responseCode;
        this.fieldName = null;
        this.customMessage = null;
    }

    public CustomException(ResponseCode responseCode, String customMessage) {
        super(customMessage);
        this.responseCode = responseCode;
        this.fieldName = null;
        this.customMessage = customMessage;
    }

    public CustomException(ResponseCode responseCode, String fieldName, String customMessage) {
        super(customMessage);
        this.responseCode = responseCode;
        this.fieldName = fieldName;
        this.customMessage = customMessage;
    }
}
