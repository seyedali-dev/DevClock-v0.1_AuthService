package com.seyed.ali.authenticationservice.keycloak.service;

import com.seyed.ali.authenticationservice.keycloak.model.domain.KeycloakUser;
import com.seyed.ali.authenticationservice.keycloak.repository.KeycloakUserRepository;
import com.seyed.ali.authenticationservice.keycloak.service.interfaces.KeycloakAdminRoleService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.core.oidc.StandardClaimNames;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;

@SuppressWarnings("preview")
@ExtendWith(MockitoExtension.class)
class UserOperationServiceImplTest {

    private @InjectMocks UserOperationServiceImpl userOperationService;
    private @Mock KeycloakAdminRoleService keycloakAdminRoleService;
    private @Mock KeycloakUserRepository keycloakUserRepository;

    @Test
    void retrieveUserTest() {
        // given
        KeycloakUser keycloakUser = new KeycloakUser();
        keycloakUser.setId("1");
        keycloakUser.setUsername("username");
        keycloakUser.setEmail("email");
        when(this.keycloakUserRepository.findById(isA(String.class)))
                .thenReturn(Optional.of(keycloakUser));

        // when
        Optional<KeycloakUser> user = this.userOperationService.retrieveUser("username");

        // then
        assertThat(user)
                .as("Must have some value")
                .isPresent();
        assertThat(user.get().getId())
                .as(STR."Must be same as: \{keycloakUser.getId()}")
                .isEqualTo(keycloakUser.getId());
    }

    @Test
    public void createNewUserTest() {
        // Arrange
        // mocking `JWT`
        Jwt jwt = Mockito.mock(Jwt.class);
        when(jwt.getSubject())
                .thenReturn("someUserId");
        when(jwt.getClaim(StandardClaimNames.GIVEN_NAME))
                .thenReturn("GIVEN_NAME");
        when(jwt.getClaim(StandardClaimNames.FAMILY_NAME))
                .thenReturn("FAMILY_NAME");
        when(jwt.getClaim(StandardClaimNames.PREFERRED_USERNAME))
                .thenReturn("PREFERRED_USERNAME");
        when(jwt.getClaim(StandardClaimNames.EMAIL))
                .thenReturn("EMAIL");
        when(jwt.getClaim(StandardClaimNames.EMAIL_VERIFIED))
                .thenReturn(true);
        when(jwt.getClaim(StandardClaimNames.ADDRESS))
                .thenReturn("ADDRESS");
        when(jwt.getClaim(StandardClaimNames.PHONE_NUMBER))
                .thenReturn("PHONE_NUMBER");
        when(jwt.getClaim(StandardClaimNames.BIRTHDATE))
                .thenReturn(LocalDateTime.of(2024, 9, 9, 0, 0));
        when(jwt.getClaim(StandardClaimNames.ZONEINFO))
                .thenReturn("ZONEINFO");

        KeycloakUser expectedUser = new KeycloakUser();
        expectedUser.setId("someUserId");
        expectedUser.setFirstName("testGivenName");

        // mocking the `defaultRole` field
        ReflectionTestUtils.setField(userOperationService, "defaultRole", "testRole");

        // mocking `KeycloakAdminRoleService#addRoleToUser(userId, roleName)`
        doNothing()
                .when(this.keycloakAdminRoleService)
                .addRoleToUser(expectedUser.getId(), "testRole");

        // mocking `KeycloakUserRepository#save(T)`
        when(keycloakUserRepository.save(any(KeycloakUser.class)))
                .thenReturn(expectedUser);

        // Act
        KeycloakUser actualUser = userOperationService.createNewUser(jwt);

        // Assert
        assertThat(expectedUser)
                .as("Must be same")
                .isEqualTo(actualUser);
        Mockito.verify(keycloakAdminRoleService, Mockito.times(1))
                .addRoleToUser(expectedUser.getId(), "testRole");
    }

}