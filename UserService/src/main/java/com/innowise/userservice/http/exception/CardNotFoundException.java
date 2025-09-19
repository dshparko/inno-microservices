package com.innowise.userservice.http.exception;

/**
 * @ClassName CardNotFoundException
 * @Description Custom runtime exception thrown when a {@link com.innowise.userservice.database.entity.Card} is not found.
 * @Author dshparko
 * @Date 11.09.2025 15:54
 * @Version 1.0
 */
public class CardNotFoundException extends RuntimeException {
    public CardNotFoundException(Long id) {
        super("Card wasn't found with id: " + id);
    }
}
