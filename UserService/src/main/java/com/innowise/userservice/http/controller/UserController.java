package com.innowise.userservice.http.controller;

import com.innowise.userservice.dto.card.CardResponse;
import com.innowise.userservice.dto.user.CreateUserRequest;
import com.innowise.userservice.dto.user.UpdateUserRequest;
import com.innowise.userservice.dto.user.UserResponse;
import com.innowise.userservice.dto.user.UserWithCardsResponse;
import com.innowise.userservice.http.exception.UserNotFoundException;
import com.innowise.userservice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
 * @ClassName UserController
 * @Description REST controller for managing {@link com.innowise.userservice.database.entity.User} entities and their associated {@link CardResponse} data.
 * Provides endpoints for user CRUD operations and card retrieval.
 * @Author dshparko
 * @Date 11.09.2025 8:52
 * @Version 1.0
 */

@Validated
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    /**
     * Retrieves a user by their unique identifier.
     *
     * @param id the ID of the user to retrieve
     * @return {@link UserWithCardsResponse} containing user data and associated cards
     * @throws UserNotFoundException if no user exists with the given ID
     */
    @GetMapping("/id/{id}")
    public ResponseEntity<UserWithCardsResponse> userById(@PathVariable("id") Long id) throws UserNotFoundException {
        return ResponseEntity.ok(userService.findById(id));
    }

    /**
     * Retrieves a user by their email address.
     *
     * @param email the email of the user to retrieve
     * @return {@link UserWithCardsResponse} containing user data and associated cards
     * @throws UserNotFoundException if no user exists with the given email
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<UserWithCardsResponse> findUserByEmail(@PathVariable("email") String email) {
        return ResponseEntity.ok(userService.findUserByEmail(email));
    }

    /**
     * Retrieves all users in the system.
     *
     * @return list of {@link UserWithCardsResponse} objects; 204 No Content if empty
     */
    @GetMapping
    public ResponseEntity<List<UserWithCardsResponse>> findAll() {
        List<UserWithCardsResponse> users = userService.findAll();
        if (users.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(users);
    }

    /**
     * Retrieves multiple users by their IDs.
     *
     * @param ids list of user IDs to retrieve
     * @return list of {@link UserWithCardsResponse} objects; 204 No Content if none found
     */
    @GetMapping("/batch")
    public ResponseEntity<List<UserWithCardsResponse>> findUsersByIds(@RequestParam List<Long> ids) {
        List<UserWithCardsResponse> users = userService.findUsersByIds(ids);
        if (users.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(users);
    }

    /**
     * Creates a new user.
     *
     * @param user DTO containing user creation data
     * @return {@link UserResponse} representing the newly created user
     */
    @PostMapping
    public ResponseEntity<UserResponse> createUser(@RequestBody @Valid CreateUserRequest user) {
        return ResponseEntity.ok(userService.createUser(user));
    }

    /**
     * Updates an existing user.
     *
     * @param user DTO containing updated user data
     * @return 200 OK if update was successful
     */
    @PutMapping
    public ResponseEntity<Void> updateUser(@RequestBody @Valid UpdateUserRequest user) {
        userService.updateUser(user);
        return ResponseEntity.ok().build();
    }

    /**
     * Deletes a user by their ID.
     *
     * @param id the ID of the user to delete
     * @return 200 OK if deletion was successful
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }
}
