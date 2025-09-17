package com.innowise.userservice.dto.user;

import com.innowise.userservice.dto.card.CardResponse;

import java.time.LocalDate;
import java.util.List;

/**
 * @ClassName UserWithCardsResponse
 * @Description * DTO representing a user along with their associated cards.
 * Includes basic personal information and a list of {@link CardResponse} objects
 * for downstream consumption in API responses or service layers.
 * @Author dshparko
 * @Date 16.09.2025 10:57
 * @Version 1.0
 */
public record UserWithCardsResponse(
        Long id,
        String name,
        String surname,
        String email,
        LocalDate birthDate,
        List<CardResponse> cards
) {
}
