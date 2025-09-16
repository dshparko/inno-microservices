package com.innowise.userservice.http.exception;

/**
 * @ClassName UserNotFoundException
 * @Description Custom runtime exception thrown when a {@link com.innowise.userservice.database.entity.User} is not found.
 * @Author dshparko
 * @Date 11.09.2025 15:50
 * @Version 1.0
 */
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String key, Object value) {
        super("User wasn't found with " + key + ": " + value);
    }
}

