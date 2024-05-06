package com.seyed.ali.authenticationservice.keycloak.service.interfaces;

import com.seyed.ali.authenticationservice.keycloak.model.domain.KeycloakUser;
import org.springframework.security.oauth2.jwt.Jwt;

/**
 * Service interface responsible for persisting user information when authenticating with Keycloak.
 * <p>
 * This service provides a single method to handle the registration of a new user.
 *
 * @author [Seyed-Ali]
 */
public interface UserPersistenceService {

    /**
     * Persists the user's information when they authenticate with Keycloak.
     * <p>
     * This method is responsible for registering a new user in the system, using the information provided in the JSON Web Token.
     * <ol>
     *     <li>Fetches the user from the database or creates a new one if not found.</li>
     *     <li>Update the user roles if necessary.</li>
     * </ol>
     *
     * @param jwt the JSON Web Token containing the user's authentication information
     * @return the persisted {@link KeycloakUser} object
     */
    KeycloakUser handleUser(Jwt jwt);

}
