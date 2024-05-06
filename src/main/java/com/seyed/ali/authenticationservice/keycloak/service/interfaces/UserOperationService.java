package com.seyed.ali.authenticationservice.keycloak.service.interfaces;


import com.seyed.ali.authenticationservice.keycloak.model.domain.KeycloakUser;
import com.seyed.ali.authenticationservice.keycloak.service.UserOperationServiceImpl;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Optional;

/**
 * Service interface for performing user operations.
 * <p>
 * This interface provides methods for retrieving users, assigning default roles, and creating new users.
 *
 * @author [Seyed-Ali]
 * @see UserOperationServiceImpl
 */
public interface UserOperationService {

    /**
     * Retrieves a user based on their ID.
     * <p>
     * This method retrieves a user from the database based on their ID, which is extracted from the JWT token.
     *
     * @param userId the ID of the user to retrieve
     * @return an Optional containing the found KeycloakUser, or empty if no user is found
     */
    Optional<KeycloakUser> retrieveUser(String userId);

    /**
     * Assigns a default role to a user.
     * <p>
     * This method assigns a default role to a user. In our application, the default role is 'user'.
     *
     * @param userId the ID of the user to whom the role will be assigned
     */
    void assignDefaultRolesToUser(String userId);

    /**
     * Creates a new user in the database using the information provided in a JWT token.
     * <p>
     * This method creates a new user in the database using the information extracted from the JWT token.
     *
     * @param jwt the JWT token containing the user's information
     * @return the KeycloakUser object that was created
     */
    KeycloakUser createNewUser(Jwt jwt);

}

