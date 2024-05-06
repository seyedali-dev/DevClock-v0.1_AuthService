package com.seyed.ali.authenticationservice.keycloak.controller;

import com.seyed.ali.authenticationservice.keycloak.model.dto.RoleDTO;
import com.seyed.ali.authenticationservice.keycloak.service.interfaces.KeycloakAdminRoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.keycloak.representations.idm.RoleRepresentation;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

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
    public ResponseEntity<String> addRoleToUser(@PathVariable String userId, @PathVariable String roleName) {
        this.keycloakAdminRoleService.addRoleToUser(userId, roleName);
        return new ResponseEntity<>("Role added to user successfully", CREATED);
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
    public ResponseEntity<Void> removeUsersRole(@PathVariable String userId, @PathVariable String roleName) {
        this.keycloakAdminRoleService.removeUsersRole(userId, roleName);
        return ResponseEntity.noContent().build();
    }

    /**
     * Retrieves a list of all roles.
     *
     * @return a response containing a list of role DTOs
     */
    @GetMapping("/list")
    public ResponseEntity<List<RoleDTO>> getRoles() {
        return ResponseEntity.ok(this.keycloakAdminRoleService.getRolesList());
    }

    /**
     * Retrieves a single role by name.
     *
     * @param roleName the name of the role to retrieve
     * @return a response containing the role DTO
     */
    @GetMapping("/{roleName}")
    public ResponseEntity<RoleDTO> getRoleName(@PathVariable String roleName) {
        return ResponseEntity.ok(this.keycloakAdminRoleService.getSingleRoleRepresentation(roleName));
    }

    /**
     * Creates a new role.
     *
     * @param roleDTO the role DTO containing the role information
     * @return a response containing the created role representation
     */
    @PostMapping
    public ResponseEntity<RoleRepresentation> createRole(@RequestBody RoleDTO roleDTO) {
        return new ResponseEntity<>(this.keycloakAdminRoleService.createRoleRepresentation(roleDTO), CREATED);
    }

    /**
     * Updates the name of an existing role.
     *
     * @param roleDTO the role DTO containing the updated role information
     * @return a response indicating that the role was updated successfully
     */
    @PutMapping
    public ResponseEntity<String> updateName(@RequestBody RoleDTO roleDTO) {
        this.keycloakAdminRoleService.updateRoleName(roleDTO);
        return new ResponseEntity<>("role updated.", CREATED);
    }

    /**
     * Deletes a role.
     *
     * @param roleName the name of the role to delete
     * @return a response indicating that the role was deleted successfully
     */
    @DeleteMapping("/{roleName}")
    public ResponseEntity<Void> deleteRole(@PathVariable String roleName) {
        this.keycloakAdminRoleService.deleteRole(roleName);
        return ResponseEntity.noContent().build();
    }

}
