package com.jashan.userservice.pojo;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorResponse {
    @JsonProperty("error_code")
    private int errorCode;

    @JsonProperty("error_message")
    private String errorMessage;

    @JsonProperty("status_code")
    private int statusCode;

    @JsonProperty("http_method")
    private String httpMethod;

    @JsonProperty("backend_message")
    private String backendMessage;

    private LocalDateTime timestamp;

    private List<String> details;

    public ErrorResponse(int errorCode, String errorMessage, int statusCode, String httpMethod, String backendMessage,
            LocalDateTime timestamp) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.statusCode = statusCode;
        this.httpMethod = httpMethod;
        this.backendMessage = backendMessage;
        this.timestamp = timestamp;
    }
}
