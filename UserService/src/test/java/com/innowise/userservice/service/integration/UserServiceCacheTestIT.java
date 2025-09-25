package com.innowise.userservice.service.integration;

import com.innowise.userservice.database.entity.User;
import com.innowise.userservice.database.repository.UserRepository;
import com.innowise.userservice.dto.user.UpdateUserRequest;
import com.innowise.userservice.dto.user.UserWithCardsResponse;
import com.innowise.userservice.service.UserService;
import com.innowise.userservice.service.config.IntegrationTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import static java.time.LocalDate.of;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserServiceCacheTestIT extends IntegrationTestBase {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CacheManager cacheManager;

    private User savedUser;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        User user = new User();
        user.setName("Darya");
        user.setSurname("Shparko");
        user.setEmail("darya@example.com");
        user.setBirthDate(of(1990, 1, 1));
        savedUser = userRepository.save(user);

        Cache userWithCards = cacheManager.getCache("userWithCards");
        if (userWithCards != null) {
            userWithCards.clear();
        }

        Cache userByEmail = cacheManager.getCache("userByEmail");
        if (userByEmail != null) {
            userByEmail.clear();
        }
    }

    @Test
    @DisplayName("findById should cache result")
    void findById_ShouldCacheResult() {
        UserWithCardsResponse response1 = userService.findById(savedUser.getId());

        assertEquals("Darya", response1.name());

        savedUser.setName("Changed");
        userRepository.save(savedUser);

        UserWithCardsResponse response2 = userService.findById(savedUser.getId());
        assertEquals("Darya", response2.name());
    }

    @Test
    @DisplayName("updateUser should evict cache")
    void updateUser_ShouldEvictCache() {
        userService.findById(savedUser.getId());

        UpdateUserRequest updateRequest = new UpdateUserRequest(
                savedUser.getId(),
                "Ivan",
                "Ivanov",
                "ivan@example.com",
                of(1995, 5, 5)
        );

        userService.updateUser(updateRequest);

        UserWithCardsResponse response = userService.findById(savedUser.getId());
        assertEquals("Ivan", response.name());
        assertEquals("Ivanov", response.surname());
    }

    @Test
    @DisplayName("deleteUser should evict cache")
    void deleteUser_ShouldEvictCache() {
        userService.findById(savedUser.getId());

        userService.deleteUser(savedUser.getId());

        assertFalse(userRepository.findById(savedUser.getId()).isPresent());

        long userId = savedUser.getId();
        assertThrows(RuntimeException.class, () -> userService.findById(userId));
    }
}
