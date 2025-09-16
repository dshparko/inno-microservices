package com.innowise.userservice.dto.error;

import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

/**
 * @ClassName ErrorResponseDto
 * @Description DTO used for exposing structured error details in API responses.
 * Encapsulates HTTP status code, error message, request path, and timestamp.
 * Serves as a standardized error payload for {@link org.springframework.web.bind.annotation.ExceptionHandler} methods.
 * @Author dshparko
 * @Date 16.09.2025 16:25
 * @Version 1.0
 */
@Getter
@Setter
public class ErrorResponseDto {
    private ZonedDateTime timestamp;
    private int status;
    private String error;
    private String path;

    public ErrorResponseDto(int status, String error, String path) {
        this.timestamp = ZonedDateTime.now();
        this.status = status;
        this.error = error;
        this.path = path;
    }
}
