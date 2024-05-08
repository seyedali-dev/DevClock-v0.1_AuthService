package com.seyed.ali.authenticationservice.keycloak.controller;

import com.seyed.ali.authenticationservice.keycloak.model.dto.UserDTO;
import com.seyed.ali.authenticationservice.keycloak.service.interfaces.KeycloakAdminUserService;
import com.seyed.ali.authenticationservice.model.response.Result;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.*;

/**
 * A RESTful resource for managing Keycloak admin users.
 * <p>
 * This resource provides endpoints for retrieving user information, creating/updating/deleting users, and managing user roles.
 *
 * @author [Seyed-Ali]
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/keycloak/user")
@SecurityRequirement(name = "Keycloak")
@Tag(
        name = "Users Resource",
        description = """
                A secure and efficient way for managing user accounts and roles.
                
                This includes CRUD operations on users, as well as assigning and revoking roles.
                
                These operations are also reflected in the Keycloak server’s database simultaneously.
                
                To ensure the security of our system, these operations should be restricted to users with the `realm_admin` role.
                
                This role represents administrative users who are trusted to manage user accounts and roles.
                """
)
public class UserResource {

    private final KeycloakAdminUserService keycloakAdminUserService;

    /**
     * Retrieves a list of all users.
     *
     * @return a list of user DTOs
     */
    @GetMapping("/list")
    public Result getUsers() {
        return new Result(
                true,
                OK,
                "List of users present in db (keycloak & app).",
                this.keycloakAdminUserService.getUserDTOList()
        );
    }

    /**
     * Retrieves a single user by ID.
     *
     * @param id the ID of the user to retrieve
     * @return a user DTO
     */
    @GetMapping("/{id}")
    public Result getUserDTO(@PathVariable String id) {
        return new Result(
                true,
                OK,
                "Found user in db (keycloak & app).",
                this.keycloakAdminUserService.getSingleUserDTO(id)
        );
    }

    /**
     * Creates a new user.
     *
     * @param userDTO the user DTO containing the user information
     * @return a response entity containing a success message and the user ID
     */
    @PostMapping
    @ResponseStatus(CREATED)
    public Result createUser(@RequestBody UserDTO userDTO) {
        return new Result(
                true,
                CREATED,
                "User created and saved in db & keycloak's db.",
                this.keycloakAdminUserService.createUserRepresentation(userDTO)
        );
    }

    // TODO: there 2 calls to db - this is not efficient. make the update method return the updated user.
    /**
     * Updates an existing user.
     *
     * @param id      the ID of the user to update
     * @param userDTO the user DTO containing the updated user information
     * @return a response entity containing a success message and the user ID
     */
    @PutMapping("/{id}")
    public Result updateUser(@PathVariable String id, @RequestBody UserDTO userDTO) {
        this.keycloakAdminUserService.updateUserRepresentation(id, userDTO);
        return new Result(
                true,
                OK,
                "User updated and saved in db & keycloak's db.",
                this.keycloakAdminUserService.getSingleUserDTO(id)
        );
    }

    /**
     * Deletes a user.
     *
     * @param id the ID of the user to delete
     * @return a response entity with no content
     */
    @DeleteMapping("/{id}")
    public Result deleteUser(@PathVariable String id) {
        this.keycloakAdminUserService.deleteUserRepresentation(id);
        return new Result(
                true,
                NO_CONTENT,
                "User deleted both from db & keycloak's db."
        );
    }

}
