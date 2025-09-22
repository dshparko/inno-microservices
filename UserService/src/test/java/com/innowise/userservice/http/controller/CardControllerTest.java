package com.innowise.userservice.http.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.innowise.userservice.dto.card.CardResponse;
import com.innowise.userservice.dto.card.CreateCardRequest;
import com.innowise.userservice.dto.card.UpdateCardRequest;
import com.innowise.userservice.service.CardService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CardController.class)
class CardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CardService cardService;

    @Autowired
    private ObjectMapper objectMapper;

    private final CardResponse sampleCard = new CardResponse(
            1L,
            "1234567890123456",
            "Ivan Ivanov",
            LocalDate.of(2030, 12, 31),
            42L
    );

    @Test
    @DisplayName("GET /cards/{id} should return card by ID")
    void getCardById_shouldReturnCard() throws Exception {
        Mockito.when(cardService.findById(1L)).thenReturn(sampleCard);

        mockMvc.perform(get("/api/v1/cards/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.number").value("1234567890123456"));
    }

    @Test
    @DisplayName("GET /cards should return all cards")
    void findAll_shouldReturnList() throws Exception {
        Mockito.when(cardService.findAll()).thenReturn(List.of(sampleCard));

        mockMvc.perform(get("/api/v1/cards"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));
    }

    @Test
    @DisplayName("GET /cards should return 204 if empty")
    void findAll_shouldReturnNoContent() throws Exception {
        Mockito.when(cardService.findAll()).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/cards"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("GET /cards/batch should return cards by IDs")
    void findCardsByIds_shouldReturnList() throws Exception {
        Mockito.when(cardService.findCardsByIds(List.of(1L, 2L))).thenReturn(List.of(sampleCard));

        mockMvc.perform(get("/api/v1/cards/batch?ids=1&ids=2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));
    }

    @Test
    @DisplayName("POST /cards should create a card")
    void createCard_shouldReturnCreatedCard() throws Exception {
        CreateCardRequest request = new CreateCardRequest(
                "1234567890123456",
                "Ivan Ivanov",
                LocalDate.of(2030, 12, 31),
                42L
        );

        Mockito.when(cardService.createCard(any())).thenReturn(sampleCard);

        mockMvc.perform(post("/api/v1/cards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    @DisplayName("PUT /cards should update a card")
    void updateCard_shouldReturnOk() throws Exception {
        UpdateCardRequest request = new UpdateCardRequest(
                1L,
                "9999888877776666",
                "Ivan Ivanov",
                LocalDate.of(2031, 1, 1),
                1L
        );

        mockMvc.perform(put("/api/v1/cards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        Mockito.verify(cardService).updateCard(request);
    }

    @Test
    @DisplayName("DELETE /cards/{id} should delete card")
    void deleteCard_shouldReturnOk() throws Exception {
        mockMvc.perform(delete("/api/v1/cards/1"))
                .andExpect(status().isOk());

        Mockito.verify(cardService).deleteById(1L);
    }

    @Test
    @DisplayName("GET /cards/user/{userId} should return user's cards")
    void getUserCards_shouldReturnList() throws Exception {
        Mockito.when(cardService.findCardsByUserId(42L)).thenReturn(List.of(sampleCard));

        mockMvc.perform(get("/api/v1/cards/user/42"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userId").value(42L));
    }
}
