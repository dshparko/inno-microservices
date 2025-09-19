package com.innowise.userservice.service;

import com.innowise.userservice.database.entity.User;
import com.innowise.userservice.database.repository.UserRepository;
import com.innowise.userservice.dto.user.CreateUserRequest;
import com.innowise.userservice.dto.user.UpdateUserRequest;
import com.innowise.userservice.dto.user.UserResponse;
import com.innowise.userservice.dto.user.UserWithCardsResponse;
import com.innowise.userservice.http.exception.UserNotFoundException;
import com.innowise.userservice.mapper.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    private User user;
    private UserResponse userResponse;
    private UserWithCardsResponse userWithCardsResponse;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(1L);
        user.setName("Darya");
        user.setSurname("Shparko");
        user.setEmail("darya@example.com");
        user.setBirthDate(LocalDate.of(1990, 1, 1));

        userResponse = new UserResponse(1L, "Darya", "Shparko", "darya@example.com", LocalDate.of(1990, 1, 1));
        userWithCardsResponse = new UserWithCardsResponse(1L, "Darya", "Shparko", "darya@example.com", LocalDate.of(1990, 1, 1), List.of());
    }

    @Test
    @DisplayName("Create user")
    void createUser_ShouldSaveAndReturnResponse() {
        CreateUserRequest request = new CreateUserRequest("Darya", "Shparko", "darya@example.com", LocalDate.of(1990, 1, 1));

        when(userMapper.mapToEntity(request)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.mapToResponse(user)).thenReturn(userResponse);

        UserResponse result = userService.createUser(request);

        assertEquals(userResponse, result);
        verify(userRepository).save(user);
        verify(userMapper).mapToEntity(request);
        verify(userMapper).mapToResponse(user);

        verifyNoMoreInteractions(userRepository, userMapper);
    }

    @Test
    @DisplayName("Find by id")
    void findById_ShouldReturnUser() {
        when(userRepository.findByIdWithCards(1L)).thenReturn(Optional.of(user));
        when(userMapper.mapToUserWithCards(user)).thenReturn(userWithCardsResponse);

        UserWithCardsResponse result = userService.findById(1L);

        assertEquals(userWithCardsResponse, result);
    }

    @Test
    @DisplayName("Find by id -> Not found")
    void findById_ShouldThrowWhenNotFound() {
        when(userRepository.findByIdWithCards(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.findById(1L));
    }

    @Test
    @DisplayName("Find by email")
    void findUserByEmail_ShouldReturnUser() {
        when(userRepository.findUserByEmail("darya@example.com")).thenReturn(Optional.of(user));
        when(userMapper.mapToUserWithCards(user)).thenReturn(userWithCardsResponse);

        UserWithCardsResponse result = userService.findUserByEmail("darya@example.com");

        assertEquals(userWithCardsResponse, result);
    }

    @Test
    @DisplayName("Find by email -> Not found")
    void findUserByEmail_ShouldThrowWhenNotFound() {
        when(userRepository.findUserByEmail("darya@example.com")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.findUserByEmail("darya@example.com"));
    }

    @Test
    @DisplayName("Find by ids")
    void findUsersByIds_ShouldReturnList() {
        when(userRepository.findByIdIn(List.of(1L))).thenReturn(List.of(user));
        when(userMapper.mapToUserWithCardsResponseList(List.of(user))).thenReturn(List.of(userWithCardsResponse));

        List<UserWithCardsResponse> result = userService.findUsersByIds(List.of(1L));

        assertEquals(1, result.size());
        assertIterableEquals(List.of(userWithCardsResponse), result);
    }

    @Test
    @DisplayName("Update user")
    void updateUser_ShouldUpdateSuccessfully() {
        UpdateUserRequest request = new UpdateUserRequest(1L, "Darya", "Shparko", "darya@example.com", LocalDate.of(1990, 1, 1));

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.updateUserById(1L, "Darya", "Shparko", "darya@example.com", LocalDate.of(1990, 1, 1)))
                .thenReturn(1);

        assertDoesNotThrow(() -> userService.updateUser(request));
        verify(userRepository).updateUserById(1L, "Darya", "Shparko", "darya@example.com", LocalDate.of(1990, 1, 1));
        verify(userRepository).findById(1L);
    }

    @Test
    @DisplayName("Update user -> Not found")
    void updateUser_ShouldThrowIfNotFound() {
        UpdateUserRequest request = new UpdateUserRequest(1L, "Darya", "Shparko", "darya@example.com", LocalDate.of(1990, 1, 1));

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.updateUser(request));
    }

    @Test
    @DisplayName("Update user -> fail")
    void updateUser_ShouldThrowIfUpdateFails() {
        UpdateUserRequest request = new UpdateUserRequest(1L, "Ivan", "Ivanov", "ivan@example.com", LocalDate.of(1990, 1, 1));

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.updateUserById(anyLong(), anyString(), anyString(), anyString(), any())).thenReturn(0);

        assertThrows(IllegalStateException.class, () -> userService.updateUser(request));
    }

    @Test
    @DisplayName("Delete user")
    void deleteUser_ShouldDeleteSuccessfully() {
        when(userRepository.findByIdWithCards(1L)).thenReturn(Optional.of(user));

        assertDoesNotThrow(() -> userService.deleteUser(1L));
        verify(userRepository).delete(user);
        verify(userRepository).findByIdWithCards(1L);
    }

    @Test
    @DisplayName("Delete -> Not found")
    void deleteUser_ShouldThrowIfNotFound() {
        when(userRepository.findByIdWithCards(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.deleteUser(1L));
    }

    @Test
    @DisplayName("Find all")
    void findAll_ShouldReturnList() {
        when(userRepository.findAll()).thenReturn(List.of(user));
        when(userMapper.mapToUserWithCardsResponseList(List.of(user))).thenReturn(List.of(userWithCardsResponse));

        List<UserWithCardsResponse> result = userService.findAll();

        assertEquals(1, result.size());
        assertIterableEquals(List.of(userWithCardsResponse), result);
    }

}