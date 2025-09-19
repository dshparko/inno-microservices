package com.innowise.userservice.dto.card;

import java.time.LocalDate;

/**
 * @ClassName CardResponse
 * @Description DTO used for exposing card data in API responses.
 * Encapsulates card number, holder name, expiration date, and associated user ID.
 * Serves as a read-only projection of {@link com.innowise.userservice.database.entity.Card}.
 * @Author dshparko
 * @Date 08.09.2025 21:30
 * @Version 1.0
 */
public record CardResponse(
        Long id,
        String number,
        String holder,
        LocalDate expirationDate,
        Long userId
) {
}
