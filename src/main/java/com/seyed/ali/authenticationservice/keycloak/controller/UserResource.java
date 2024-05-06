package com.seyed.ali.authenticationservice.keycloak.controller;

import com.seyed.ali.authenticationservice.keycloak.model.dto.UserDTO;
import com.seyed.ali.authenticationservice.keycloak.service.interfaces.KeycloakAdminUserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequiredArgsConstructor
@RequestMapping("/keycloak/user")
@SecurityRequirement(name = "Keycloak")
@Tag(
        name = "Users Resource",
        description = """
                A secure and efficient way for managing user accounts and roles. \
                This includes CRUD operations on users, as well as assigning and revoking roles. \
                These operations are also reflected in the Keycloak server’s database simultaneously. \
                To ensure the security of our system, these operations should be restricted to users with the `realm_admin` role. \
                This role represents administrative users who are trusted to manage user accounts and roles.
                """
)
public class UserResource {

    private final KeycloakAdminUserService keycloakAdminUserService;

    @GetMapping("/list")
    public List<UserDTO> getUsers() {
        return this.keycloakAdminUserService.getUserDTOList();
    }

    @GetMapping("/{id}")
    public UserDTO getUserDTO(@PathVariable String id) {
        return this.keycloakAdminUserService.getSingleUserDTO(id);
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public ResponseEntity<Map<String, String>> createUser(@RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(this.keycloakAdminUserService.createUserRepresentation(userDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, String>> updateUser(@PathVariable String id, @RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(this.keycloakAdminUserService.updateUserRepresentation(id, userDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> DeleteUser(@PathVariable String id) {
        this.keycloakAdminUserService.deleteUserRepresentation(id);
        return ResponseEntity.noContent().build();
    }

}
