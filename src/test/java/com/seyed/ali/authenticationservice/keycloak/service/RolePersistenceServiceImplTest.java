package com.seyed.ali.authenticationservice.keycloak.service;

import com.seyed.ali.authenticationservice.keycloak.model.domain.KeycloakRoles;
import com.seyed.ali.authenticationservice.keycloak.model.domain.KeycloakUser;
import com.seyed.ali.authenticationservice.keycloak.repository.KeycloakRolesRepository;
import com.seyed.ali.authenticationservice.keycloak.repository.KeycloakUserRepository;
import com.seyed.ali.authenticationservice.keycloak.util.KeycloakSecurityUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RolePersistenceServiceImplTest {

    private @InjectMocks RolePersistenceServiceImpl rolePersistenceService;
    private @Mock KeycloakSecurityUtil keycloakSecurityUtil;
    private @Mock KeycloakRolesRepository keycloakRolesRepository;
    private @Mock KeycloakUserRepository keycloakUserRepository;

    @Test
    void updateRolesIfNecessaryTest() {
        // given
        Jwt jwt = Mockito.mock(Jwt.class);
        String mockRoleName = "default_role_test";
        Collection<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(mockRoleName));
        when(this.keycloakSecurityUtil.extractAuthorities(jwt))
                .thenReturn(authorities);

        KeycloakRoles keycloakRoles = new KeycloakRoles();
        keycloakRoles.setRoleName(mockRoleName);
        when(this.keycloakRolesRepository.findByRoleName(isA(String.class)))
                .thenReturn(Optional.empty());

        KeycloakUser keycloakUser = new KeycloakUser();
        keycloakUser.setKeycloakRoles(Set.of(keycloakRoles));
        when(this.keycloakUserRepository.save(isA(KeycloakUser.class)))
                .thenReturn(keycloakUser);

        // when
        this.rolePersistenceService.updateRolesIfNecessary(jwt, keycloakUser);

        // then
        verify(this.keycloakSecurityUtil, times(1))
                .extractAuthorities(jwt);
        verify(this.keycloakRolesRepository, times(1))
                .findByRoleName(mockRoleName);
        verify(this.keycloakUserRepository, times(1))
                .save(keycloakUser);
    }

}