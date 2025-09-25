package com.innowise.userservice.dto.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * @ClassName UpdateUserRequest
 * @Description DTO used for accepting user update input from API clients.
 * Encapsulates user ID, name, surname, email, and birth date.
 * Serves as a write-only projection for updating existing {@link com.innowise.userservice.database.entity.User} entities.
 * @Author dshparko
 * @Date 08.09.2025 21:30
 * @Version 1.0
 */
public record UpdateUserRequest(
        @NotNull(message = "User ID is required")
        Long id,

        @NotBlank(message = "Name is required")
        String name,

        @NotBlank(message = "Surname is required")
        String surname,

        @Email(message = "Email must be valid")
        @NotBlank(message = "Email is required") String email,

        @Past(message = "Birth date must be in the past")
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate birthDate) implements Serializable {
}
