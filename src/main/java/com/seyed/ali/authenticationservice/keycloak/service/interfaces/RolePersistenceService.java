package com.seyed.ali.authenticationservice.keycloak.service.interfaces;

import com.seyed.ali.authenticationservice.keycloak.model.domain.KeycloakUser;
import com.seyed.ali.authenticationservice.keycloak.service.RolePersistenceServiceImpl;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

/**
 * This service class is responsible for persisting user roles in the system.
 * It implements the RolePersistenceService interface.
 *
 * @author [Seyed-Ali]
 * @see RolePersistenceServiceImpl
 */
public interface RolePersistenceService {

    /**
     * Updates the roles of a user if necessary. The roles are extracted from the provided JWT token.
     * If a role does not exist in the database, it is created.
     *
     * @param jwt  The JWT token containing the user's roles
     * @param user The user whose roles are to be updated
     * @see RolePersistenceServiceImpl#createRoleFromAuthorities(GrantedAuthority, KeycloakUser)
     */
    void updateRolesIfNecessary(Jwt jwt, KeycloakUser user);

}
