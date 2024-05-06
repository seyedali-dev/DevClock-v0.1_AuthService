package com.seyed.ali.authenticationservice.keycloak.service.interfaces;

import com.seyed.ali.authenticationservice.keycloak.model.dto.UserDTO;

import java.util.List;
import java.util.Map;

/**
 * Service interface for managing Keycloak admin users.
 * <p>
 * This interface provides methods for retrieving user information, creating/updating/deleting users.
 *
 * @author [Seyed-Ali]
 */
public interface KeycloakAdminUserService {

    /**
     * Retrieves a list of all users.
     *
     * @return a list of user DTOs
     */
    List<UserDTO> getUserDTOList();

    /**
     * Retrieves a single user by ID.
     *
     * @param id the ID of the user to retrieve
     * @return a user DTO
     */
    UserDTO getSingleUserDTO(String id);

    /**
     * Creates a new user.
     *
     * @param userDTO the user DTO containing the user information
     * @return a map containing the user ID and a success message
     */
    Map<String, String> createUserRepresentation(UserDTO userDTO);

    /**
     * Updates an existing user.
     *
     * @param userId  the ID of the user to update
     * @param userDTO the user DTO containing the updated user information
     * @return a map containing the user ID and a success message
     */
    Map<String, String> updateUserRepresentation(String userId, UserDTO userDTO);

    /**
     * Deletes a user.
     *
     * @param userId the ID of the user to delete
     */
    void deleteUserRepresentation(String userId);

}
