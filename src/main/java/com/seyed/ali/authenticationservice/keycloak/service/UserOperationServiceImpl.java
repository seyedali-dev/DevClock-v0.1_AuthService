package com.seyed.ali.authenticationservice.keycloak.service;

import com.seyed.ali.authenticationservice.keycloak.model.domain.KeycloakUser;
import com.seyed.ali.authenticationservice.keycloak.repository.KeycloakUserRepository;
import com.seyed.ali.authenticationservice.keycloak.service.interfaces.KeycloakAdminRoleService;
import com.seyed.ali.authenticationservice.keycloak.service.interfaces.UserOperationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.core.oidc.StandardClaimNames;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserOperationServiceImpl implements UserOperationService {

    private @Value("${keycloak.user.default-role}") String defaultRole;

    private final KeycloakAdminRoleService keycloakAdminRoleService;
    private final KeycloakUserRepository keycloakUserRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<KeycloakUser> retrieveUser(String userId) {
        return this.keycloakUserRepository.findById(userId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void assignDefaultRolesToUser(String userId) {
        this.keycloakAdminRoleService.addRoleToUser(userId, this.defaultRole);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public KeycloakUser createNewUser(Jwt jwt) {
        KeycloakUser user = new KeycloakUser();
        user.setId(jwt.getSubject());
        user.setFirstName(jwt.getClaim(StandardClaimNames.GIVEN_NAME));
        user.setLastName(jwt.getClaim(StandardClaimNames.FAMILY_NAME));
        user.setUsername(jwt.getClaim(StandardClaimNames.PREFERRED_USERNAME));
        user.setEmail(jwt.getClaim(StandardClaimNames.EMAIL));
        user.setEmailVerified(jwt.getClaim(StandardClaimNames.EMAIL_VERIFIED));
        user.setAddress(jwt.getClaim(StandardClaimNames.ADDRESS));
        user.setPhoneNumber(jwt.getClaim(StandardClaimNames.PHONE_NUMBER));
        user.setBirthDate(jwt.getClaim(StandardClaimNames.BIRTHDATE));
        user.setZoneInfo(jwt.getClaim(StandardClaimNames.ZONEINFO));

        this.keycloakAdminRoleService.addRoleToUser(user.getId(), this.defaultRole);

        return this.keycloakUserRepository.save(user);
    }

}
