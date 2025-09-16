package com.innowise.userservice.database.repository;

import com.innowise.userservice.database.entity.Card;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * @ClassName CardRepository
 * @Description Repository interface for accessing and manipulating {@link Card} entities.
 * Supports CRUD operations, custom JPQL and native queries for card updates,
 * and retrieval by user or card identifiers.
 * @Author dshparko
 * @Date 08.09.2025 15:34
 * @Version 1.0
 */
@Repository
public interface CardRepository extends JpaRepository<Card, Long> {

    /**
     * Get card by users identifier
     *
     * @param userId - user identifier
     * @return Optional with card if it exists or else empty
     */

    @Query("SELECT c " +
            "FROM Card c " +
            "WHERE c.user.id = :userId")
    Optional<Card> findCardByUserId(@Param("userId") Long userId);

    /**
     * Get cards list by their identifiers
     *
     * @param ids - cards identifiers
     * @return List with cards if they exist or else empty
     */
    @Query("SELECT c FROM Card c WHERE c.id IN :ids")
    List<Card> findCardsByIdIn(List<Long> ids);

    /**
     * Update card
     *
     * @param id             - Id of the card to update
     * @param userId         - New user id
     * @param number         - New card number
     * @param holder         - New cardholder
     * @param expirationDate - New expiration date of card
     */
    @Modifying
    @Transactional
    @Query("UPDATE Card c " +
            "SET  c.user.id = :userId, c.number = :number, c.holder = :holder, c.expirationDate = :expirationDate " +
            "WHERE c.id = :id")
    int updateCardById(@Param("id") Long id,
                       @Param("userId") Long userId,
                       @Param("number") String number,
                       @Param("holder") String holder,
                       @Param("expirationDate") LocalDate expirationDate);
}
