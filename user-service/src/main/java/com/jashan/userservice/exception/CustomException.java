package com.jashan.userservice.exception;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class CustomException extends RuntimeException {

    @JsonProperty("error_code")
    private final int errorCode;

    @JsonProperty("status_code")
    private final int statusCode;

    @JsonProperty("backend_message")
    private final String backendMessage;

    private final HttpStatus status;

    public CustomException(int errorCode, String message, HttpStatus status, String backendMessage) {
        super(message);
        this.errorCode = errorCode;
        this.statusCode = status.value();
        this.backendMessage = backendMessage;
        this.status = status;
    }
}
