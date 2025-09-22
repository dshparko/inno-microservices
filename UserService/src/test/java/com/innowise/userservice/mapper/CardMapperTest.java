package com.innowise.userservice.mapper;

import com.innowise.userservice.database.entity.Card;
import com.innowise.userservice.database.entity.User;
import com.innowise.userservice.dto.card.CardResponse;
import com.innowise.userservice.dto.card.CreateCardRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {CardMapperImpl.class})
class CardMapperTest {

    @Autowired
    private CardMapper cardMapper;

    @Test
    @DisplayName("mapToResponse should map Card to CardResponse correctly")
    void mapToResponse_shouldMapCorrectly() {
        User user = new User();
        user.setId(42L);

        Card card = new Card();
        card.setId(1L);
        card.setNumber("1234567890123456");
        card.setExpirationDate(LocalDate.of(2030, 12, 31));
        card.setUser(user);

        CardResponse response = cardMapper.mapToResponse(card);

        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.number()).isEqualTo("1234567890123456");
        assertThat(response.expirationDate()).isEqualTo(LocalDate.of(2030, 12, 31));
        assertThat(response.userId()).isEqualTo(42L);
    }

    @Test
    @DisplayName("mapToEntity should map CreateCardRequest to Card correctly")
    void mapToEntity_shouldMapCorrectly() {
        CreateCardRequest request = new CreateCardRequest(
                "9876543210987654",
                "Ivan Ivanov",
                LocalDate.of(2028, 6, 15),
                99L

        );

        Card card = cardMapper.mapToEntity(request);

        assertThat(card.getId()).isNull();
        assertThat(card.getNumber()).isEqualTo("9876543210987654");
        assertThat(card.getExpirationDate()).isEqualTo(LocalDate.of(2028, 6, 15));
        assertThat(card.getUser()).isNotNull();
        assertThat(card.getUser().getId()).isEqualTo(99L);
    }

    @Test
    @DisplayName("mapToResponseList should map list of Cards to list of CardResponses")
    void mapToResponseList_shouldMapListCorrectly() {
        User user = new User();
        user.setId(7L);

        Card card1 = new Card();
        card1.setId(1L);
        card1.setNumber("1111111111111111");
        card1.setExpirationDate(LocalDate.of(2025, 1, 1));
        card1.setUser(user);

        Card card2 = new Card();
        card2.setId(2L);
        card2.setNumber("2222222222222222");
        card2.setExpirationDate(LocalDate.of(2026, 2, 2));
        card2.setUser(user);

        List<CardResponse> responses = cardMapper.mapToResponseList(List.of(card1, card2));

        assertThat(responses).hasSize(2);
        assertThat(responses.get(0).userId()).isEqualTo(7L);
        assertThat(responses.get(1).userId()).isEqualTo(7L);
    }
}
