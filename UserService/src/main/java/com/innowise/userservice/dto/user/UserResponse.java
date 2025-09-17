package com.innowise.userservice.dto.user;

import java.time.LocalDate;

/**
 * @ClassName UserResponse
 * @Description DTO used for exposing user data in API responses.
 * Encapsulates user ID, name, surname, email, and birth date.
 * Serves as a read-only projection of {@link com.innowise.userservice.database.entity.User}.
 * @Author dshparko
 * @Date 08.09.2025 21:30
 * @Version 1.0
 */

public record UserResponse(
        Long id,
        String name,
        String surname,
        String email,
        LocalDate birthDate
) {
}
