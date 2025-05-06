package com.jashan.product_service.constant;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public enum ErrorCodeEnum {

    // Generic Errors
    GENERIC_ERROR(1000, "An unexpected error occurred"),
    VALIDATION_ERROR(1001, "Validation failed"),
    UNAUTHORIZED(1002, "Unauthorized access. Please provide a valid token."),
    FORBIDDEN(1003, "You do not have permission to perform this action."),

    // Product Specific Errors
    PRODUCT_NOT_FOUND(2001, "Product not found"),
    PRODUCT_ALREADY_EXISTS(2002, "Product with this name already exists"),
    INSUFFICIENT_STOCK(2003, "Insufficient stock for the requested quantity"),
    INVALID_PRODUCT_DATA(2004, "Invalid product data provided"),

    // JWT Specific Errors (optional in product service, but useful for gateway
    // filtering)
    TOKEN_EXPIRED(3001, "JWT token has expired"),
    INVALID_SIGNATURE(3002, "Invalid JWT signature"),
    INVALID_TOKEN(3003, "Malformed or unsupported JWT token"),
    TOKEN_MISSING(3004, "JWT token is missing or empty"),

    ACCESS_DENIED(4001, "You are not authorized to perform this action.");

    private final int errorCode;
    private final String errorMessage;

    ErrorCodeEnum(int errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
