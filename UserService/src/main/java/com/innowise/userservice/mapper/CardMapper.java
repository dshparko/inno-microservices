package com.innowise.userservice.mapper;

import com.innowise.userservice.database.entity.Card;
import com.innowise.userservice.dto.card.CardResponse;
import com.innowise.userservice.dto.card.CreateCardRequest;
import com.innowise.userservice.dto.card.UpdateCardRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * @ClassName CardMapper
 * @Description MapStruct-based mapper for converting between {@link Card} entities and DTOs.
 * Provides bidirectional mapping methods for request and response models.
 * Used to decouple persistence layer from API contracts and reduce boilerplate conversion logic.
 * @Author dshparko
 * @Date 14.09.2025 19:09
 * @Version 1.0
 */

@Mapper(componentModel = "spring")
public interface CardMapper {
    /**
     * Maps a {@link Card} entity to a {@link CardResponse} DTO.
     *
     * @param entity the card entity to convert
     * @return the mapped response DTO
     */
    @Mapping(source = "user.id", target = "userId")
    CardResponse mapToResponse(Card entity);

    /**
     * Maps a {@link CreateCardRequest} DTO to a {@link Card} entity.
     * Used during card creation.
     *
     * @param request the DTO containing card creation data
     * @return the mapped card entity
     */
    Card mapToEntity(CreateCardRequest request);

    /**
     * Maps an {@link UpdateCardRequest} DTO to a {@link Card} entity.
     * Used during card update operations.
     *
     * @param request the DTO containing updated card data
     * @return the mapped card entity
     */
    Card mapToEntity(UpdateCardRequest request);

    /**
     * Maps a list of {@link Card} entities to a list of {@link CardResponse} DTOs.
     *
     * @param entities the list of card entities to convert
     * @return the list of mapped response DTOs
     */
    List<CardResponse> mapToResponseList(List<Card> entities);

}
