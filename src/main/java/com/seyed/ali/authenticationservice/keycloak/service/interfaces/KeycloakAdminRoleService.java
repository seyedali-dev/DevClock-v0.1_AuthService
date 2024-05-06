package com.seyed.ali.authenticationservice.keycloak.service.interfaces;

import com.seyed.ali.authenticationservice.keycloak.model.dto.RoleDTO;
import org.keycloak.representations.idm.RoleRepresentation;

import java.util.List;

/**
 * Service interface for managing Keycloak admin roles.
 * <p>
 * This interface provides methods for retrieving role information, creating/updating/deleting roles, and managing user roles.
 *
 * @author [Seyed-Ali]
 */
public interface KeycloakAdminRoleService {

    /**
     * Retrieves a list of all roles.
     *
     * @return a list of role DTOs
     */
    List<RoleDTO> getRolesList();

    /**
     * Retrieves a single role by name.
     *
     * @param roleName the name of the role to retrieve
     * @return a role DTO
     */
    RoleDTO getSingleRoleRepresentation(String roleName);

    /**
     * Creates a new role.
     *
     * @param roleDTO the role DTO containing the role information
     * @return the created role representation
     */
    RoleRepresentation createRoleRepresentation(RoleDTO roleDTO);

    /**
     * Updates the name of an existing role.
     *
     * @param roleDTO the role DTO containing the updated role information
     */
    void updateRoleName(RoleDTO roleDTO);

    /**
     * Deletes a role.
     *
     * @param roleName the name of the role to delete
     */
    void deleteRole(String roleName);

    /**
     * Retrieves a list of roles for a user.
     *
     * @param id the ID of the user
     * @return a list of role DTOs
     */
    List<RoleDTO> getUserRoles(String id);

    /**
     * Adds a role to a user.
     *
     * @param userId   the ID of the user
     * @param roleName the name of the role to add
     */
    void addRoleToUser(String userId, String roleName);

    /**
     * Adds a default role to a user.
     *
     * @param id the ID of the user
     */
    void addRoleToUser(String id);

    /**
     * Removes a role from a user.
     *
     * @param id       the ID of the user
     * @param roleName the name of the role to remove
     */
    void removeUsersRole(String id, String roleName);

}
