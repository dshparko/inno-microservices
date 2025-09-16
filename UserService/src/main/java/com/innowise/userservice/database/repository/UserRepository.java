package com.innowise.userservice.database.repository;

import com.innowise.userservice.database.entity.User;
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
 * @ClassName UserRepository
 * @Description Provides methods for CRUD operations.
 * Custom queries are implemented using both JPQL and native SQL.
 * @Author dshparko
 * @Date 08.09.2025 15:34
 * @Version 1.0
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * Find users by a list of ids.
     *
     * @param ids List of user ids.
     * @return List of {@link User} entities matching the given ids.
     * If none found, returns an empty list.
     */
    List<User> findByIdIn(List<Long> ids);

    /**
     * Find a user by their email address using a native SQL query.
     *
     * @param email Email address of the user.
     * @return Optional containing the {@link User} entity if found, or empty if no user exists with the given email.
     */
    @Query(value = "SELECT u.id, u.name, u.email, u.surname, u.birth_date " +
            "FROM users u " +
            "WHERE u.email =:email", nativeQuery = true)
    Optional<User> findUserByEmail(@Param("email") String email);

    /**
     * Update a user's information by their ID.
     * <p>
     * This method updates the {@code name}, {@code surname}, {@code email}, and {@code birthDate} fields
     * of the user with the given {@code id}.
     *
     * @param id        Id of the user to update.
     * @param name      New first name of the user.
     * @param surname   New surname of the user.
     * @param email     New email address of the user.
     * @param birthDate New birth date of the user.
     */
    @Modifying
    @Transactional
    @Query("UPDATE User u " +
            "SET u.name = :name, u.surname = :surname, u.email = :email," +
            " u.birthDate = :birthDate " +
            "WHERE u.id = :id")
    void updateUserById(@Param("id") Long id,
                        @Param("name") String name,
                        @Param("surname") String surname,
                        @Param("email") String email,
                        @Param("birthDate") LocalDate birthDate);

}
