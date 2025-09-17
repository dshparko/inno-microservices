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

    @GetMapping("/id={id}")
    public ResponseEntity<CardResponse> cardById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(cardService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<CardResponse>> findAll() {
        List<CardResponse> cards = cardService.findAll();
        if (cards.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(cards);
    }

    @GetMapping("/batch")
    public ResponseEntity<List<CardResponse>> findCardsByIds(@RequestParam List<Long> ids) {
        List<CardResponse> cards = cardService.findCardsByIds(ids);
        if (cards.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(cards);
    }

    @PostMapping
    public ResponseEntity<CardResponse> createCard(@RequestBody @Valid CreateCardRequest request) {
        return ResponseEntity.ok(cardService.createCard(request));
    }

    @PutMapping
    public ResponseEntity<CardResponse> updateCard(@RequestBody @Valid UpdateCardRequest request) {
        cardService.updateCard(request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteCard(@PathVariable("id") Long id) {
        cardService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
