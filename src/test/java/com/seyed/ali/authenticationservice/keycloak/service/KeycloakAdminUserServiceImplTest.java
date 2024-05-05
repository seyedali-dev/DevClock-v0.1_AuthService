package com.seyed.ali.authenticationservice.keycloak.service;

import com.seyed.ali.authenticationservice.keycloak.model.dto.UserDTO;
import com.seyed.ali.authenticationservice.keycloak.util.KeycloakSecurityUtil;
import com.seyed.ali.authenticationservice.keycloak.util.converter.UserRepresentationToUserDtoConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SuppressWarnings("preview")
@ExtendWith(MockitoExtension.class)
class KeycloakAdminUserServiceImplTest {

    //<editor-fold desc="fields">
    private @InjectMocks KeycloakAdminUserServiceImpl keycloakAdminUserService;
    private @Mock UserRepresentationToUserDtoConverter userRepresentationToUserDtoConverter;
    private @Mock Keycloak keycloak;
    private @Mock KeycloakSecurityUtil keycloakSecurityUtil;
    private @Mock UsersResource usersResource;
    //</editor-fold>

    @BeforeEach
    void setUp() {
        // Initialize the keycloak mock
        this.keycloak = mock(Keycloak.class);
        RealmResource realmResource = mock(RealmResource.class);

        when(this.keycloakSecurityUtil.createOrGetKeycloakInstance())
                .thenReturn(this.keycloak);
        when(this.keycloak.realm(null))
                .thenReturn(realmResource);
        when(realmResource.users())
                .thenReturn(this.usersResource);
    }

    @Test
    public void getUserDTOListTest() {
        // Arrange
        UserRepresentation userRepresentation = new UserRepresentation();
        List<UserRepresentation> userRepresentationList = Collections.singletonList(userRepresentation);
        when(this.usersResource.list())
                .thenReturn(userRepresentationList);

        UserDTO userDTO = new UserDTO(
                "1",
                "John",
                "Doe",
                "some@email.com",
                "johndoe",
                null
        );
        when(this.userRepresentationToUserDtoConverter.convert(userRepresentation))
                .thenReturn(userDTO);

        // Act
        List<UserDTO> userDTOList = this.keycloakAdminUserService.getUserDTOList();

        // Assert
        assertThat(userDTOList)
                .isNotEmpty()
                .as("Must have 1 users data")
                .hasSize(1);
        assertThat(userDTOList.getFirst())
                .as(STR."Should be: {\{userDTO}}")
                .isEqualTo(userDTO);
    }

}