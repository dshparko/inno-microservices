package com.innowise.userservice.http.controller;

import com.innowise.userservice.dto.card.CardResponse;
import com.innowise.userservice.dto.user.CreateUserRequest;
import com.innowise.userservice.dto.user.UpdateUserRequest;
import com.innowise.userservice.dto.user.UserResponse;
import com.innowise.userservice.dto.user.UserWithCardsResponse;
import com.innowise.userservice.http.exception.UserNotFoundException;
import com.innowise.userservice.service.CardService;
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
    private final CardService cardService;

    @GetMapping("/id={id}")
    public ResponseEntity<UserWithCardsResponse> userById(@PathVariable("id") Long id) throws UserNotFoundException {
        return ResponseEntity.ok(userService.findById(id));
    }

    @GetMapping("/email={email}")
    public ResponseEntity<UserWithCardsResponse> findUserByEmail(@PathVariable("email") String email) {
        return ResponseEntity.ok(userService.findUserByEmail(email));
    }

    @GetMapping
    public ResponseEntity<List<UserWithCardsResponse>> findAll() {
        List<UserWithCardsResponse> users = userService.findAll();
        if (users.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(users);
    }

    @GetMapping("/batch")
    public ResponseEntity<List<UserWithCardsResponse>> findUsersByIds(@RequestParam List<Long> ids) {
        List<UserWithCardsResponse> users = userService.findUsersByIds(ids);
        if (users.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(users);
    }

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@RequestBody @Valid CreateUserRequest user) {
        return ResponseEntity.ok(userService.createUser(user));
    }

    @PutMapping
    public ResponseEntity<Void> updateUser(@RequestBody @Valid UpdateUserRequest user) {
        userService.updateUser(user);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }

    /**
     * Retrieves all cards associated with a specific user.
     *
     * @param userId unique identifier of the user
     * @return list of {@link CardResponse} objects linked to the user
     */
    @GetMapping("/{userId}/cards")
    public ResponseEntity<List<CardResponse>> getUserCards(@PathVariable Long userId) {
        List<CardResponse> cards = cardService.findCardsByUserId(userId);
        return ResponseEntity.ok(cards);
    }


}
