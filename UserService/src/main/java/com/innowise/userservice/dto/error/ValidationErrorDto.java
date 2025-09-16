package com.innowise.userservice.dto.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName ValidationErrorDto
 * @Description DTO used for exposing field-level validation errors in API responses.
 * Encapsulates the name of the invalid field, the validation message, and the rejected value.
 * Serves as a structured component of {@link com.innowise.userservice.dto.error.ErrorResponseDto} for detailed feedback.
 * @Author dshparko
 * @Date 16.09.2025 15:34
 * @Version 1.0
 */

@AllArgsConstructor
@Getter
@Setter
public class ValidationErrorDto {
    private String field;
    private String message;
    private String rejectedValue;
}
