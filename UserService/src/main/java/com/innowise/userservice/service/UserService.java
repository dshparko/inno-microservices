package com.innowise.userservice.service;

import com.innowise.userservice.database.entity.User;
import com.innowise.userservice.database.repository.UserRepository;
import com.innowise.userservice.dto.user.CreateUserRequest;
import com.innowise.userservice.dto.user.UpdateUserRequest;
import com.innowise.userservice.dto.user.UserResponse;
import com.innowise.userservice.dto.user.UserWithCardsResponse;
import com.innowise.userservice.http.exception.UserNotFoundException;
import com.innowise.userservice.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @ClassName UserService
 * @Description Service layer for managing user operations.
 * Handles creation, retrieval, update, and deletion of users.
 * @Author dshparko
 * @Date 11.09.2025 7:48
 * @Version 1.0
 */

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    /**
     * Creates a new user based on the provided request DTO.
     *
     * @param request DTO containing user creation data
     * @return mapped {@link UserResponse} representing the saved user
     */
    @Transactional
    public UserResponse createUser(CreateUserRequest request) {
        User user = userMapper.mapToEntity(request);
        return userMapper.mapToResponse(userRepository.save(user));
    }

    /**
     * Retrieves a user by ID and caches the result.
     *
     * @param id unique identifier of the user
     * @return mapped {@link UserWithCardsResponse} if found
     * @throws UserNotFoundException if no user exists with the given ID
     */
    @Cacheable(value = "userWithCardsResponse", key = "#id")
    public UserWithCardsResponse findById(Long id) throws UserNotFoundException {
        User user = userRepository.findByIdWithCards(id)
                .orElseThrow(() -> new UserNotFoundException("Id", id));
        return userMapper.mapToUserWithCards(user);
    }

    /**
     * Retrieves multiple users by their IDs.
     *
     * @param ids list of user IDs to fetch
     * @return list of mapped {@link UserWithCardsResponse} objects
     */
    public List<UserWithCardsResponse> findUsersByIds(List<Long> ids) {
        return userMapper.mapToUserWithCardsResponseList(userRepository.findByIdIn(ids));
    }

    /**
     * Retrieves a user by email.
     *
     * @param email email address of the user
     * @return mapped {@link UserWithCardsResponse} if found
     * @throws UserNotFoundException if no user exists with the given email
     */
    @Cacheable(value = "userByEmail", key = "#email")
    public UserWithCardsResponse findUserByEmail(String email) {
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Email", email));
        return userMapper.mapToUserWithCards(user);
    }

    /**
     * Updates an existing user based on the provided request DTO.
     * Evicts cached entry to ensure consistency.
     *
     * @param request DTO containing updated user data
     * @throws UserNotFoundException if no user exists with the given ID
     */
    @Caching(evict = {
            @CacheEvict(value = "userWithCards", key = "#request.id()"),
            @CacheEvict(value = "userByEmail", key = "#request.email()")
    })
    @Transactional
    public void updateUser(UpdateUserRequest request) {
        User user = userRepository.findById(request.id())
                .orElseThrow(() -> new UserNotFoundException("Id", request.id()));

        int updated = userRepository.updateUserById(
                user.getId(),
                request.name(),
                request.surname(),
                request.email(),
                request.birthDate()
        );

        if (updated == 0) {
            throw new IllegalStateException("User update failed for id: " + request.id());
        }
    }

    /**
     * Deletes a user by ID.
     * Evicts cached entry to ensure consistency.
     *
     * @param id unique identifier of the user to delete
     * @throws UserNotFoundException if no user exists with the given ID
     */
    @CacheEvict(value = "userWithCards", key = "#id")
    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findByIdWithCards(id)
                .orElseThrow(() -> new UserNotFoundException("Id", id));

        userRepository.delete(user);
    }

    /**
     * Retrieves all users in the system.
     *
     * @return list of mapped {@link UserWithCardsResponse} objects
     */
    public List<UserWithCardsResponse> findAll() {
        return userMapper.mapToUserWithCardsResponseList(userRepository.findAll());
    }

}
