package com.innowise.userservice.dto.card;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

/**
 * @ClassName CreateCardRequest
 * @Description DTO used for accepting card creation input from API clients.
 * Encapsulates card number, holder name, expiration date, and associated user ID.
 * Serves as a write-only projection for creating {@link com.innowise.userservice.database.entity.Card} entities.
 * @Author dshparko
 * @Date 08.09.2025 21:30
 * @Version 1.0
 */
public record CreateCardRequest(
        @NotBlank(message = "Card number is required")
        String number,

        @NotBlank(message = "Holder name is required")
        String holder,

        @Future(message = "Expiration date must be in the future")
        LocalDate expirationDate,

        @NotNull(message = "User ID is required")
        Long userId
) {
}
