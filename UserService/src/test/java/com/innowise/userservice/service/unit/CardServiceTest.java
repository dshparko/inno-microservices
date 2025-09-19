package com.innowise.userservice.service.unit;

import com.innowise.userservice.database.entity.Card;
import com.innowise.userservice.database.entity.User;
import com.innowise.userservice.database.repository.CardRepository;
import com.innowise.userservice.database.repository.UserRepository;
import com.innowise.userservice.dto.card.CardResponse;
import com.innowise.userservice.dto.card.CreateCardRequest;
import com.innowise.userservice.dto.card.UpdateCardRequest;
import com.innowise.userservice.http.exception.CardNotFoundException;
import com.innowise.userservice.http.exception.UserNotFoundException;
import com.innowise.userservice.mapper.CardMapper;
import com.innowise.userservice.service.CardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class CardServiceTest {

    @Mock
    private CardRepository cardRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CardMapper cardMapper;

    @InjectMocks
    private CardService cardService;

    private User user;
    private Card card;
    private CardResponse cardResponse;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(1L);
        user.setName("Darya");

        card = new Card();
        card.setId(100L);
        card.setNumber("1234-5678");
        card.setHolder("Darya");
        card.setExpirationDate(LocalDate.of(2030, 1, 1));
        card.setUser(user);

        cardResponse = new CardResponse(
                100L,
                "1234-5678",
                "Darya",
                LocalDate.of(2030, 1, 1),
                1L
        );
    }

    @Test
    @DisplayName("Create card")
    void createCard_ShouldSaveAndReturnResponse() {
        CreateCardRequest request = new CreateCardRequest("1234-5678", "Darya", LocalDate.of(2030, 1, 1), 1L);

        when(cardMapper.mapToEntity(request)).thenReturn(card);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(cardRepository.save(card)).thenReturn(card);
        when(cardMapper.mapToResponse(card)).thenReturn(cardResponse);

        CardResponse result = cardService.createCard(request);

        assertEquals(cardResponse, result);
        verify(userRepository).findById(1L);
        verify(cardRepository).save(card);
    }

    @Test
    @DisplayName("Create card -> user not found")
    void createCard_ShouldThrowIfUserNotFound() {
        CreateCardRequest request = new CreateCardRequest("1234-5678", "Darya", LocalDate.of(2030, 1, 1), 1L);

        when(cardMapper.mapToEntity(request)).thenReturn(card);
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> cardService.createCard(request));
    }

    @Test
    @DisplayName("Find by id")
    void findById_ShouldReturnCard() {
        when(cardRepository.findById(100L)).thenReturn(Optional.of(card));
        when(cardMapper.mapToResponse(card)).thenReturn(cardResponse);

        CardResponse result = cardService.findById(100L);

        assertEquals(cardResponse, result);
    }

    @Test
    @DisplayName("Find by id -> Not found")
    void findById_ShouldThrowIfNotFound() {
        when(cardRepository.findById(100L)).thenReturn(Optional.empty());

        assertThrows(CardNotFoundException.class, () -> cardService.findById(100L));
    }

    @Test
    @DisplayName("Find cards by ids")
    void findCardsByIds_ShouldReturnList() {
        when(cardRepository.findCardsByIdIn(List.of(100L))).thenReturn(List.of(card));
        when(cardMapper.mapToResponseList(List.of(card))).thenReturn(List.of(cardResponse));

        List<CardResponse> result = cardService.findCardsByIds(List.of(100L));

        assertEquals(1, result.size());
        assertEquals(cardResponse, result.get(0));
    }

    @Test
    @DisplayName("Update card")
    void updateCard_ShouldUpdateSuccessfully() {
        UpdateCardRequest request = new UpdateCardRequest(100L, "9999", "Darya", LocalDate.of(2035, 1, 1), 1L);

        when(cardRepository.existsById(100L)).thenReturn(true);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(cardRepository.updateCardById(100L, 1L, "9999", "Darya", LocalDate.of(2035, 1, 1)))
                .thenReturn(1);

        assertDoesNotThrow(() -> cardService.updateCard(request));
        verify(cardRepository).updateCardById(100L, 1L, "9999", "Darya", LocalDate.of(2035, 1, 1));
    }

    @Test
    @DisplayName("Update card -> not found")
    void updateCard_ShouldThrowIfCardNotFound() {
        UpdateCardRequest request = new UpdateCardRequest(200L, "9999", "Darya", LocalDate.of(2035, 1, 1), 1L);

        when(cardRepository.existsById(200L)).thenReturn(false);

        assertThrows(CardNotFoundException.class, () -> cardService.updateCard(request));
    }

    @Test
    @DisplayName("Update card -> user not found")
    void updateCard_ShouldThrowIfUserNotFound() {
        UpdateCardRequest request = new UpdateCardRequest(100L, "9999", "Darya", LocalDate.of(2035, 1, 1), 99L);

        when(cardRepository.existsById(100L)).thenReturn(true);
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> cardService.updateCard(request));
    }

    @Test
    @DisplayName("Update card -> update failed")
    void updateCard_ShouldThrowIfUpdateFails() {
        UpdateCardRequest request = new UpdateCardRequest(100L, "9999", "Darya", LocalDate.of(2035, 1, 1), 1L);

        when(cardRepository.existsById(100L)).thenReturn(true);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(cardRepository.updateCardById(anyLong(), anyLong(), anyString(), anyString(), any()))
                .thenReturn(0);

        assertThrows(IllegalStateException.class, () -> cardService.updateCard(request));
    }

    @Test
    @DisplayName("Delete card")
    void deleteById_ShouldDeleteSuccessfully() {
        when(cardRepository.findById(100L)).thenReturn(Optional.of(card));

        assertDoesNotThrow(() -> cardService.deleteById(100L));
        verify(cardRepository).deleteById(100L);
    }

    @Test
    @DisplayName("Delete card -> not found")
    void deleteById_ShouldThrowIfNotFound() {
        when(cardRepository.findById(100L)).thenReturn(Optional.empty());

        assertThrows(CardNotFoundException.class, () -> cardService.deleteById(100L));
    }

    @Test
    @DisplayName("Find all cards")
    void findAll_ShouldReturnList() {
        when(cardRepository.findAll()).thenReturn(List.of(card));
        when(cardMapper.mapToResponseList(List.of(card))).thenReturn(List.of(cardResponse));

        List<CardResponse> result = cardService.findAll();

        assertEquals(1, result.size());
        assertEquals(cardResponse, result.get(0));
    }

    @Test
    @DisplayName("Find cards by user id")
    void findCardsByUserId_ShouldReturnList() {
        when(cardRepository.findCardsByUserId(1L)).thenReturn(List.of(card));
        when(cardMapper.mapToResponseList(List.of(card))).thenReturn(List.of(cardResponse));

        List<CardResponse> result = cardService.findCardsByUserId(1L);

        assertEquals(1, result.size());
        assertEquals(cardResponse, result.get(0));
    }
}