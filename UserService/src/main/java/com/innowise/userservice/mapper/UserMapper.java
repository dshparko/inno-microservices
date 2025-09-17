package com.innowise.userservice.mapper;

import com.innowise.userservice.database.entity.User;
import com.innowise.userservice.dto.user.CreateUserRequest;
import com.innowise.userservice.dto.user.UserResponse;
import com.innowise.userservice.dto.user.UserWithCardsResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * @ClassName UserMapper
 * @Description MapStruct-based mapper for converting between {@link User} entities and DTOs.
 * Provides bidirectional mapping methods for request and response models.
 * Used to decouple persistence layer from API contracts and reduce boilerplate conversion logic.
 * @Author dshparko
 * @Date 14.09.2025 19:09
 * @Version 1.0
 */
@Mapper(componentModel = "spring")
public interface UserMapper {
    /**
     * Maps a {@link User} entity to a {@link UserResponse} DTO.
     *
     * @param entity the user entity to convert
     * @return the mapped response DTO
     */
    UserResponse mapToResponse(User entity);

    /**
     * Maps a {@link CreateUserRequest} DTO to a {@link User} entity.
     * Used during user creation.
     *
     * @param request the DTO containing user creation data
     * @return the mapped user entity
     */
    User mapToEntity(CreateUserRequest request);

    /**
     * Maps a list of {@link User} entities to a list of {@link UserWithCardsResponse} DTOs.
     *
     * @param entities the list of user entities to convert
     * @return the list of mapped response DTOs
     */
    List<UserWithCardsResponse> mapToUserWithCardsResponseList(List<User> entities);

    /**
     * Maps a {@link User} entity to a {@link UserWithCardsResponse} DTO.
     * <p>
     * Transforms the {@code userCards} field from the entity into the {@code cards} field in the response,
     * enabling structured transfer of user data along with associated card information.
     *
     * @param entity the {@link User} entity to map
     * @return a {@link UserWithCardsResponse} containing user details and mapped card list
     */
    @Mapping(source = "userCards", target = "cards")
    UserWithCardsResponse mapToUserWithCards(User entity);
}
