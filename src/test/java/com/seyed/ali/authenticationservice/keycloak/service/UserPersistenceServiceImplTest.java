package com.seyed.ali.authenticationservice.keycloak.service;

import com.seyed.ali.authenticationservice.keycloak.model.domain.KeycloakUser;
import com.seyed.ali.authenticationservice.keycloak.repository.KeycloakUserRepository;
import com.seyed.ali.authenticationservice.keycloak.service.interfaces.RolePersistenceService;
import com.seyed.ali.authenticationservice.keycloak.service.interfaces.UserOperationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserPersistenceServiceImplTest {

    //<editor-fold desc="fields">
    private @InjectMocks UserPersistenceServiceImpl userPersistenceService;
    private @Mock UserOperationService userOperationService;
    private @Mock RolePersistenceService rolePersistenceService;
    private @Mock KeycloakUserRepository keycloakUserRepository;
    //</editor-fold>

    @Test
    void handleUser_UserExists() {
        // given
        Jwt jwt = Mockito.mock(Jwt.class);
        String id = "subject!";
        when(jwt.getSubject())
                .thenReturn(id);

        KeycloakUser keycloakUser = new KeycloakUser();
        keycloakUser.setId(id);
        keycloakUser.setEmail("some@email.com");
        when(this.userOperationService.retrieveUser(id))
                .thenReturn(Optional.of(keycloakUser));

        doNothing()
                .when(this.userOperationService)
                .assignDefaultRolesToUser(id);

        doNothing()
                .when(this.rolePersistenceService)
                .updateRolesIfNecessary(jwt, keycloakUser);
        when(this.keycloakUserRepository.save(isA(KeycloakUser.class)))
                .thenReturn(keycloakUser);
        // when
        KeycloakUser handledUser = this.userPersistenceService.handleUser(jwt);

        // then
        assertThat(handledUser)
                .as("Must not be null")
                .isNotNull();
        assertThat(handledUser.getId())
                .as("Must be 'subject!'")
                .isEqualTo(id);
    }

    @Test
    void handleUser_NewUser() {
        // given
        Jwt jwt = Mockito.mock(Jwt.class);
        String id = "subject!";
        when(jwt.getSubject())
                .thenReturn(id);

        KeycloakUser keycloakUser = new KeycloakUser();
        keycloakUser.setId(id);
        keycloakUser.setEmail("some@email.com");
        // mock for `UserOperationService#retrieveUser(id)`
        when(this.userOperationService.retrieveUser(id))
                .thenReturn(Optional.empty());

        // mock for the log
        when(jwt.getClaim("email"))
                .thenReturn("some@email.com");

        // mock for `UserOperationService#assignDefaultRolesToUser(id)`
        doNothing()
                .when(this.userOperationService)
                .assignDefaultRolesToUser(id);

        // mock for `UserOperationService#createNewUser(jwt)`
        when(this.userOperationService.createNewUser(jwt))
                .thenReturn(keycloakUser);

        // mock for saving user
        when(this.keycloakUserRepository.save(isA(KeycloakUser.class)))
                .thenReturn(keycloakUser);
        // when
        KeycloakUser handledUser = this.userPersistenceService.handleUser(jwt);

        // then
        assertThat(handledUser)
                .as("Must not be null")
                .isNotNull();
        assertThat(handledUser.getId())
                .as("Must be 'subject!'")
                .isEqualTo(id);
    }

}