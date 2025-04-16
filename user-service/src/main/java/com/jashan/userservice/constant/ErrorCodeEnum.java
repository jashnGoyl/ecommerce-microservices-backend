package com.jashan.userservice.constant;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public enum ErrorCodeEnum {

    // Auth & User Errors
    GENERIC_ERROR(1000, "An unexpected error occurred"),
    USER_ALREADY_EXISTS(1001, "User already exists"),
    USER_NOT_FOUND(1002, "User not found"),
    INVALID_CREDENTIALS(1003, "Invalid email or password"),
    AUTHENTICATION_FAILURE(1004, "Authentication failed"),
    UNAUTHORIZED(1005, "Unauthorized access. Please provide a valid token."),
    VALIDATION_ERROR(1006, "Validation failed"),

    // JWT Specific Errors
    TOKEN_EXPIRED(2001, "JWT token has expired"),
    INVALID_SIGNATURE(2002, "Invalid JWT signature"),
    INVALID_TOKEN(2003, "Malformed or unsupported JWT token"),
    TOKEN_MISSING(2004, "JWT token is missing or empty");

    private final int errorCode;
    private final String errorMessage;

    ErrorCodeEnum(int errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
