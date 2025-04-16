package com.jashan.userservice.constant;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public enum DatabaseErrorCodeEnum {

    CONFIGURATION_ERROR(20001, "Database configuration error"),
    CONSTRAINT_VIOLATION(20002, "Database constraint violation"),
    CONNECTION_FAILURE(20003, "Failed to connect to the database"),
    SQL_SYNTAX_ERROR(20004, "SQL syntax error");

    private final int errorCode;
    private final String errorMessage;

    DatabaseErrorCodeEnum(int errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
