package com.seyed.ali.authenticationservice.keycloak.controller;

import com.seyed.ali.authenticationservice.keycloak.model.dto.RoleDTO;
import com.seyed.ali.authenticationservice.keycloak.service.interfaces.KeycloakAdminRoleService;
import com.seyed.ali.authenticationservice.model.response.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.*;

/**
 * REST controller for managing roles in Keycloak.
 * <p>
 * This controller provides endpoints for adding/removing roles from users, creating/updating/deleting roles, and retrieving role information.
 *
 * @author [Seyed-Ali]
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/keycloak/role")
@SecurityRequirement(name = "Keycloak")
@Tag(name = "Roles Resource")
@PreAuthorize("hasRole('realm_admin')")
public class RoleResource {

    private final KeycloakAdminRoleService keycloakAdminRoleService;

    /**
     * Adds a role to a user.
     *
     * @param userId   the ID of the user to add the role to
     * @param roleName the name of the role to add
     * @return a response indicating that the role was added successfully
     */
    @PostMapping("/{userId}/{roleName}")
    @Operation(description = "Add role to a user")
    public Result addRoleToUser(@PathVariable String userId, @PathVariable String roleName) {
        this.keycloakAdminRoleService.addRoleToUser(userId, roleName);
        return new Result(
                true,
                CREATED,
                "Role: '" + roleName + "' added to user: '" + userId + "' successfully."
        );
    }

    /**
     * Removes a role from a user.
     *
     * @param userId   the ID of the user to remove the role from
     * @param roleName the name of the role to remove
     * @return a response indicating that the role was removed successfully
     */
    @DeleteMapping("/{userId}/{roleName}")
    @Operation(description = "Remove role of a user")
    public Result removeUsersRole(@PathVariable String userId, @PathVariable String roleName) {
        this.keycloakAdminRoleService.removeUsersRole(userId, roleName);
        return new Result(
                true,
                NO_CONTENT,
                "Role: '" + roleName + "' removed from user: '" + userId + "' successfully."
        );
    }

    /**
     * Retrieves a list of all roles.
     *
     * @return a response containing a list of role DTOs
     */
    @GetMapping("/list")
    public Result getRoles() {
        return new Result(
                true,
                OK,
                "List of Roles present in db (keycloak & app).",
                this.keycloakAdminRoleService.getRolesList()
        );
    }

    /**
     * Retrieves a single role by name.
     *
     * @param roleName the name of the role to retrieve
     * @return a response containing the role DTO
     */
    @GetMapping("/{roleName}")
    public Result getRoleName(@PathVariable String roleName) {
        return new Result(
                true,
                OK,
                "Found Role.",
                this.keycloakAdminRoleService.getSingleRoleRepresentation(roleName)
        );
    }

    /**
     * Creates a new role.
     *
     * @param roleDTO the role DTO containing the role information
     * @return a response containing the created role representation
     */
    @PostMapping
    public Result createRole(@RequestBody RoleDTO roleDTO) {
        return new Result(
                true,
                CREATED,
                "Role created and saved successfully.",
                this.keycloakAdminRoleService.createRoleRepresentation(roleDTO)
        );
    }

    /**
     * Updates the name of an existing role.
     *
     * @param roleDTO the role DTO containing the updated role information
     * @return a response indicating that the role was updated successfully
     */
    @PutMapping
    public Result updateName(@RequestBody RoleDTO roleDTO) {
        this.keycloakAdminRoleService.updateRoleName(roleDTO);
        return new Result(
                true,
                OK,
                "Role updated successfully."
        );
    }

    /**
     * Deletes a role.
     *
     * @param roleName the name of the role to delete
     * @return a response indicating that the role was deleted successfully
     */
    @DeleteMapping("/{roleName}")
    public Result deleteRole(@PathVariable String roleName) {
        this.keycloakAdminRoleService.deleteRole(roleName);
        return new Result(
                true,
                NO_CONTENT,
                "Role deleted from db (keycloak & app)."
        );
    }

}
