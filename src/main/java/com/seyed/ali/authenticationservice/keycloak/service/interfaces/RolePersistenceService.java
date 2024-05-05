package com.seyed.ali.authenticationservice.keycloak.service.interfaces;

import com.seyed.ali.authenticationservice.keycloak.model.domain.KeycloakUser;
import org.springframework.security.oauth2.jwt.Jwt;

public interface RolePersistenceService {

    void updateRolesIfNecessary(Jwt jwt, KeycloakUser user);

}
