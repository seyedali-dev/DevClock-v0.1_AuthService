package com.seyed.ali.authenticationservice.keycloak.service;

import com.seyed.ali.authenticationservice.keycloak.model.dto.UserDTO;
import com.seyed.ali.authenticationservice.keycloak.util.KeycloakSecurityUtil;
import com.seyed.ali.authenticationservice.keycloak.util.converter.UserDTOToUserRepresentationConverter;
import com.seyed.ali.authenticationservice.keycloak.util.converter.UserRepresentationToUserDtoConverter;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KeycloakAdminUserServiceImplTest {

    //<editor-fold desc="fields">
    private @InjectMocks KeycloakAdminUserServiceImpl keycloakAdminUserService;
    private @Mock UserRepresentationToUserDtoConverter userRepresentationToUserDtoConverter;
    private @Mock UserDTOToUserRepresentationConverter userDTOToUserRepresentationConverter;
    private @Mock Keycloak keycloak;
    private @Mock KeycloakSecurityUtil keycloakSecurityUtil;
    private @Mock UsersResource usersResource;
    private @Mock UserDTO userDTO;
    //</editor-fold>

    //<editor-fold desc="setup">
    @BeforeEach
    void setUp() {
        this.userDTO = new UserDTO(
                "1",
                "John",
                "Doe",
                "some@email.com",
                "johndoe",
                null
        );

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
    //</editor-fold>

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
                .as("Should be: " + userDTO)
                .isEqualTo(userDTO);
    }

    @Test
    void getSingleUserDTOTest() {
        // given
        UserRepresentation userRepresentation = new UserRepresentation();
        UserResource userResource = mock(UserResource.class);

        when(this.usersResource.get(isA(String.class)))
                .thenReturn(userResource);
        when(userResource.toRepresentation())
                .thenReturn(userRepresentation);
        when(this.userRepresentationToUserDtoConverter.convert(userRepresentation))
                .thenReturn(userDTO);

        // when
        UserDTO foundUserDTO = this.keycloakAdminUserService.getSingleUserDTO("some_user");

        // then
        assertThat(foundUserDTO)
                .isNotNull()
                .isEqualTo(this.userDTO);
    }

    @Test
    public void testCreateUserRepresentation() {
        // given
        // mocking `UserDTOToUserRepresentationConverter#convert(UserDTO)`
        UserRepresentation userRepresentation = mock(UserRepresentation.class);
        when(this.userDTOToUserRepresentationConverter.convert(isA(UserDTO.class)))
                .thenReturn(userRepresentation);

        // mocking `UsersResource#create(UserRepresentation)`
        Response response = mock(Response.class);
        when(this.usersResource.create(userRepresentation))
                .thenReturn(response);

        // mocking `Response.Status.CREATED#getStatusCode()`
        String userId = "some_id";
        when(response.getStatus())
                .thenReturn(Response.Status.CREATED.getStatusCode());

        // mocking `Response#getLocation()`
        when(response.getLocation())
                .thenReturn(URI.create("/some_path/" + userId));

        Map<String, String> expectedResponse = new HashMap<>();
        expectedResponse.put("message", "User created!");
        expectedResponse.put("userId", userId);

        // when
        Map<String, String> actualResponse = this.keycloakAdminUserService.createUserRepresentation(this.userDTO);

        // then
        assertThat(actualResponse)
                .as("Must be same")
                .isEqualTo(expectedResponse);
    }

    @Test
    public void updateUserRepresentationTest() {
        // given
        String someUserId = "some_id";
        // mocking `UserDTOToUserRepresentationConverter#convert(UserDTO)`
        UserRepresentation userRepresentation = mock(UserRepresentation.class);
        when(this.userDTOToUserRepresentationConverter.convert(isA(UserDTO.class)))
                .thenReturn(userRepresentation);

        // mocking `UsersResource#get(String)`
        UserResource userResource = mock(UserResource.class);
        when(this.usersResource.get(isA(String.class)))
                .thenReturn(userResource);

        // mocking `UserResource#update(UserRepresentation)`
        doNothing()
                .when(userResource)
                .update(isA(UserRepresentation.class));

        Map<String, String> expectedResponse = new HashMap<>();
        expectedResponse.put("message", "User updated!");
        expectedResponse.put("userId", someUserId);
        // when
        Map<String, String> actualResponse = this.keycloakAdminUserService.updateUserRepresentation(someUserId, this.userDTO);

        // then
        assertThat(actualResponse)
                .as("Must be same")
                .isEqualTo(expectedResponse);
    }

    @Test
    void deleteUserRepresentationTest() {
        // given
        String someUserId = "some_ID";

        // when
        this.keycloakAdminUserService.deleteUserRepresentation(someUserId);

        // then
        verify(this.usersResource, times(1))
                .delete(isA(String.class));
    }

}