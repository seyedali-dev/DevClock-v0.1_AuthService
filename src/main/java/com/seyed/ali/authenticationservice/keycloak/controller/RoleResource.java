package com.seyed.ali.authenticationservice.keycloak.controller;

import com.seyed.ali.authenticationservice.keycloak.model.dto.RoleDTO;
import com.seyed.ali.authenticationservice.keycloak.service.KeycloakAdminRoleServiceImpl;
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

@RestController
@RequiredArgsConstructor
@RequestMapping("/keycloak/role")
@SecurityRequirement(name = "Keycloak")
@Tag(name = "Roles Resource")
@PreAuthorize("hasRole('realm_admin')")
public class RoleResource {

    private final KeycloakAdminRoleServiceImpl keycloakAdminRoleService;

    @PostMapping("/{userId}/{roleName}")
    @Operation(description = "Add role to a user")
    public ResponseEntity<String> addRoleToUser(@PathVariable String userId, @PathVariable String roleName) {
        this.keycloakAdminRoleService.addRoleToUser(userId, roleName);
        return new ResponseEntity<>("Role added to user successfully", CREATED);
    }

    @DeleteMapping("/{userId}/{roleName}")
    @Operation(description = "Remove role of a user")
    public ResponseEntity<Void> removeUsersRole(@PathVariable String userId, @PathVariable String roleName) {
        this.keycloakAdminRoleService.removeUsersRole(userId, roleName);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/list")
    public ResponseEntity<List<RoleDTO>> getRoles() {
        return ResponseEntity.ok(this.keycloakAdminRoleService.getRolesList());
    }

    @GetMapping("/{roleName}")
    public ResponseEntity<RoleDTO> getRoleName(@PathVariable String roleName) {
        return ResponseEntity.ok(this.keycloakAdminRoleService.getSingleRoleRepresentation(roleName));
    }

    @PostMapping
    public ResponseEntity<RoleRepresentation> createRole(@RequestBody RoleDTO roleDTO) {
        return new ResponseEntity<>(this.keycloakAdminRoleService.createRoleRepresentation(roleDTO), CREATED);
    }

    @PutMapping
    public ResponseEntity<String> updateName(@RequestBody RoleDTO roleDTO) {
        this.keycloakAdminRoleService.updateRoleName(roleDTO);
        return new ResponseEntity<>("role updated.", CREATED);
    }

    @DeleteMapping("/{roleName}")
    public ResponseEntity<Void> deleteRole(@PathVariable String roleName) {
        this.keycloakAdminRoleService.deleteRole(roleName);
        return ResponseEntity.noContent().build();
    }

}

