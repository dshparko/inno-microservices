package com.innowise.userservice.http.controller;

import com.innowise.userservice.dto.card.CardResponse;
import com.innowise.userservice.dto.card.CreateCardRequest;
import com.innowise.userservice.dto.card.UpdateCardRequest;
import com.innowise.userservice.service.CardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName CardController
 * @Description REST controller for managing {@link com.innowise.userservice.database.entity.Card} entities and their associated {@link CardResponse} data.
 * Provides endpoints for user CRUD operations and card retrieval.
 * @Author dshparko
 * @Date 12.09.2025 20:49
 * @Version 1.0
 */
@Validated
@RestController
@RequestMapping("/api/v1/cards")
@RequiredArgsConstructor
public class CardController {

    private final CardService cardService;

    /**
     * Retrieves a card by its unique identifier.
     *
     * @param id the ID of the card to retrieve
     * @return {@link CardResponse} representing the card
     */
    @GetMapping("/{id}")
    public ResponseEntity<CardResponse> cardById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(cardService.findById(id));
    }

    /**
     * Retrieves all cards in the system.
     *
     * @return list of {@link CardResponse} objects; 204 No Content if none found
     */
    @GetMapping
    public ResponseEntity<List<CardResponse>> findAll() {
        List<CardResponse> cards = cardService.findAll();
        if (cards.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(cards);
    }

    /**
     * Retrieves multiple cards by their IDs.
     *
     * @param ids list of card IDs to retrieve
     * @return list of {@link CardResponse} objects; 204 No Content if none found
     */
    @GetMapping("/batch")
    public ResponseEntity<List<CardResponse>> findCardsByIds(@RequestParam List<Long> ids) {
        List<CardResponse> cards = cardService.findCardsByIds(ids);
        if (cards.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(cards);
    }

    /**
     * Creates a new card.
     *
     * @param request DTO containing card creation data
     * @return {@link CardResponse} representing the newly created card
     */
    @PostMapping
    public ResponseEntity<CardResponse> createCard(@RequestBody @Valid CreateCardRequest request) {
        return ResponseEntity.ok(cardService.createCard(request));
    }

    /**
     * Updates an existing card.
     *
     * @param request DTO containing updated card data
     * @return 200 OK if update was successful
     */
    @PutMapping
    public ResponseEntity<CardResponse> updateCard(@RequestBody @Valid UpdateCardRequest request) {
        cardService.updateCard(request);
        return ResponseEntity.ok().build();
    }

    /**
     * Deletes a card by its ID.
     *
     * @param id the ID of the card to delete
     * @return 200 OK if deletion was successful
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteCard(@PathVariable("id") Long id) {
        cardService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    /**
     * Retrieves all cards associated with a specific user.
     *
     * @param userId the ID of the user whose cards to retrieve
     * @return list of {@link CardResponse} objects linked to the user
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<CardResponse>> getUserCards(@PathVariable Long userId) {
        List<CardResponse> cards = cardService.findCardsByUserId(userId);
        return ResponseEntity.ok(cards);
    }
}
