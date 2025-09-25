package com.innowise.userservice.mapper;

import com.innowise.userservice.database.entity.Card;
import com.innowise.userservice.database.entity.User;
import com.innowise.userservice.dto.user.CreateUserRequest;
import com.innowise.userservice.dto.user.UserResponse;
import com.innowise.userservice.dto.user.UserWithCardsResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {UserMapperImpl.class})
class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    @DisplayName("mapToResponse should map User to UserResponse correctly")
    void mapToResponse_shouldMapCorrectly() {
        User user = new User();
        user.setId(1L);
        user.setName("Darya");
        user.setSurname("Shparko");
        user.setEmail("darya@example.com");
        user.setBirthDate(LocalDate.of(1990, 1, 1));

        UserResponse response = userMapper.mapToResponse(user);

        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.name()).isEqualTo("Darya");
        assertThat(response.surname()).isEqualTo("Shparko");
        assertThat(response.email()).isEqualTo("darya@example.com");
        assertThat(response.birthDate()).isEqualTo(LocalDate.of(1990, 1, 1));
    }

    @Test
    @DisplayName("mapToEntity should map CreateUserRequest to User correctly")
    void mapToEntity_shouldMapCorrectly() {
        CreateUserRequest request = new CreateUserRequest(
                "Darya",
                "Shparko",
                "darya@example.com",
                LocalDate.of(1990, 1, 1)
        );

        User user = userMapper.mapToEntity(request);

        assertThat(user.getId()).isNull();
        assertThat(user.getName()).isEqualTo("Darya");
        assertThat(user.getSurname()).isEqualTo("Shparko");
        assertThat(user.getEmail()).isEqualTo("darya@example.com");
        assertThat(user.getBirthDate()).isEqualTo(LocalDate.of(1990, 1, 1));
        assertThat(user.getUserCards().isEmpty());
    }

    @Test
    @DisplayName("mapToUserWithCards should map User with cards correctly")
    void mapToUserWithCards_shouldMapCorrectly() {
        Card card = new Card();
        card.setId(101L);
        card.setNumber("1234567891011124");
        card.setExpirationDate(LocalDate.of(2030, 12, 31));

        User user = new User();
        user.setId(1L);
        user.setName("Darya");
        user.setSurname("Shparko");
        user.setEmail("darya@example.com");
        user.setBirthDate(LocalDate.of(1990, 1, 1));
        user.setUserCards(List.of(card));

        UserWithCardsResponse response = userMapper.mapToUserWithCards(user);

        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.cards().size() == 1);
        assertThat(response.cards().getFirst().number()).isEqualTo("1234567891011124");
    }

    @Test
    @DisplayName("mapToUserWithCardsResponseList should map list of Users correctly")
    void mapToUserWithCardsResponseList_shouldMapListCorrectly() {
        User user = new User();
        user.setId(1L);
        user.setName("Darya");
        user.setSurname("Shparko");
        user.setEmail("darya@example.com");
        user.setBirthDate(LocalDate.of(1990, 1, 1));
        user.setUserCards(List.of());

        List<UserWithCardsResponse> responses = userMapper.mapToUserWithCardsResponseList(List.of(user));

        assertThat(responses.size() == 1);
        assertThat(responses.getFirst().email()).isEqualTo("darya@example.com");
    }
}