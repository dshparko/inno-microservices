package com.innowise.userservice.service;

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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @ClassName CardService
 * @Description Service layer for managing {@link Card} entities.
 * Provides operations for creating, retrieving, updating, and deleting cards.
 * @Author dshparko
 * @Date 12.09.2025 20:49
 * @Version 1.0
 */
@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class CardService {

    private final CardRepository cardRepository;
    private final UserRepository userRepository;
    private final CardMapper cardMapper;

    /**
     * Creates a new card based on the provided request DTO.
     *
     * @param request DTO containing card creation data
     * @return mapped {@link CardResponse} representing the saved card
     */
    @Transactional
    public CardResponse createCard(CreateCardRequest request) {
        Card card = cardMapper.mapToEntity(request);

        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new UserNotFoundException("id", request.userId()));

        user.addCard(card);
        return cardMapper.mapToResponse(cardRepository.save(card));
    }

    /**
     * Retrieves a card by its ID.
     *
     * @param id unique identifier of the card
     * @return mapped {@link CardResponse} if found
     * @throws CardNotFoundException if no card exists with the given ID
     */
    public CardResponse findById(Long id) {
        Card card = cardRepository.findById(id)
                .orElseThrow(() -> new CardNotFoundException(id));
        return cardMapper.mapToResponse(card);
    }

    /**
     * Retrieves multiple cards by their IDs.
     *
     * @param ids list of card IDs to fetch
     * @return list of mapped {@link CardResponse} objects
     */
    public List<CardResponse> findCardsByIds(List<Long> ids) {
        return cardMapper.mapToResponseList(cardRepository.findCardsByIdIn(ids));
    }

    /**
     * Updates an existing card based on the provided request DTO.
     * Ensures the card exists before performing the update.
     *
     * @param request DTO containing updated card data
     * @throws CardNotFoundException    if no card exists with the given ID
     * @throws IllegalArgumentException if the associated user is null
     */
    @Transactional
    public void updateCard(UpdateCardRequest request) {
        if (!cardRepository.existsById(request.id())) {
            throw new CardNotFoundException(request.id());
        }

        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new UserNotFoundException("id", request.userId()));

        int updated = cardRepository.updateCardById(
                request.id(),
                user.getId(),
                request.number(),
                request.holder(),
                request.expirationDate()
        );

        if (updated == 0) {
            throw new IllegalStateException("Card update failed for id: " + request.id());
        }
    }

    /**
     * Deletes a card by its ID.
     * Ensures the card exists before deletion.
     *
     * @param id unique identifier of the card to delete
     * @throws CardNotFoundException if no card exists with the given ID
     */
    @Transactional
    public void deleteById(Long id) {
        Card card = cardRepository.findById(id)
                .orElseThrow(() -> new CardNotFoundException(id));

        User user = card.getUser();
        if (user != null && user.getUserCards().contains(card)) {
            user.removeCard(card);
        }

        cardRepository.deleteById(id);
    }

    /**
     * Retrieves all cards in the system.
     *
     * @return list of mapped {@link CardResponse} objects
     */
    public List<CardResponse> findAll() {
        return cardMapper.mapToResponseList(cardRepository.findAll());
    }

    /**
     * Retrieves all cards for the selected user by id
     *
     * @param userId - Users id
     * @return list of mapped {@link CardResponse} objects
     */
    public List<CardResponse> findCardsByUserId(Long userId) {
        List<Card> cards = cardRepository.findCardsByUserId(userId);

        return cardMapper.mapToResponseList(cards);
    }

}
