package com.seyed.ali.authenticationservice.keycloak.service;

import com.seyed.ali.authenticationservice.keycloak.model.domain.KeycloakRoles;
import com.seyed.ali.authenticationservice.keycloak.model.domain.KeycloakUser;
import com.seyed.ali.authenticationservice.keycloak.repository.KeycloakRolesRepository;
import com.seyed.ali.authenticationservice.keycloak.repository.KeycloakUserRepository;
import com.seyed.ali.authenticationservice.keycloak.service.interfaces.RolePersistenceService;
import com.seyed.ali.authenticationservice.keycloak.util.KeycloakSecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class RolePersistenceServiceImpl implements RolePersistenceService {

    private final KeycloakSecurityUtil keycloakSecurityUtil;
    private final KeycloakRolesRepository keycloakRolesRepository;
    private final KeycloakUserRepository keycloakUserRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    @Cacheable(
            key = "#user.id",
            cacheNames = "role-cache"
    )
    public void updateRolesIfNecessary(Jwt jwt, KeycloakUser user) {
        // Extract authorities from jwt token
        Collection<GrantedAuthority> grantedAuthorities = this.keycloakSecurityUtil.extractAuthorities(jwt);
        Set<KeycloakRoles> rolesSet = new HashSet<>();
        log.info("Authorities: {}", grantedAuthorities);

        // Iterate through the authorities and create a new authority if it doesn't exist in the database
        grantedAuthorities.forEach(grantedAuthority -> {
            String authority = grantedAuthority.getAuthority();
            Optional<KeycloakRoles> foundAuthority = this.keycloakRolesRepository.findByRoleName(authority);

            // If the authority is not found in the database, create a new one
            if (foundAuthority.isEmpty()) {
                log.info("Role not found in cache or database. Creating new role in DB: {}", authority.replace("ROLE_", ""));
                KeycloakRoles roleFromAuthorities = this.createRoleFromAuthorities(grantedAuthority, user);
                this.keycloakRolesRepository.save(roleFromAuthorities);
                rolesSet.add(roleFromAuthorities);
            }
        });

        log.info("Role fetched from _DataBase_ and is cached. No further logs will be generated for this operation.");
        user.setKeycloakRoles(rolesSet);
        this.keycloakUserRepository.save(user);
    }

    /**
     * Creates a new KeycloakRoles entity from a GrantedAuthority and associates it with a user.
     *
     * @param grantedAuthority The authority to be converted into a role
     * @param user             The user to be associated with the role
     * @return The created KeycloakRoles entity
     */
    private KeycloakRoles createRoleFromAuthorities(GrantedAuthority grantedAuthority, KeycloakUser user) {
        KeycloakRoles keycloakRoles = new KeycloakRoles();
        keycloakRoles.setRoleId(UUID.randomUUID().toString());
        keycloakRoles.setRoleName(grantedAuthority.getAuthority());
        keycloakRoles.setKeycloakUser(Set.of(user));
        return keycloakRoles;
    }

}

