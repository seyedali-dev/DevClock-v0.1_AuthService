package com.seyed.ali.authenticationservice.keycloak.service.interfaces;


import com.seyed.ali.authenticationservice.keycloak.model.domain.KeycloakUser;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Optional;

public interface UserOperationService {

    /**
     * Retrieves a user based on their ID.
     *
     * @param userId The ID of the user, extracted from the JWT token
     * @return An {@code Optional} containing the found {@code KeycloakUser}, or empty if no user is found
     * @see . the test of this method <pre>UserOperationServiceTest#retrieveUserTest()</pre>
     */
    Optional<KeycloakUser> retrieveUser(String userId);

    /**
     * Assigns a default role to a user.
     * <p>
     * In our application, we assign a default role to all users. In this case, the default role is 'user'.
     *
     * @param userId The ID of the user to whom the role will be assigned
     */
    void assignDefaultRolesToUser(String userId);

    /**
     * Creates a new user in the database using the information provided in a JWT token.
     *
     * @param jwt The JWT token containing the user's information
     * @return The {@code KeycloakUser} object that was created
     * @see . the test of this method <pre>UserOperationServiceTest#testCreateNewUserTest()</pre>
     */
    KeycloakUser createNewUser(Jwt jwt);
}

