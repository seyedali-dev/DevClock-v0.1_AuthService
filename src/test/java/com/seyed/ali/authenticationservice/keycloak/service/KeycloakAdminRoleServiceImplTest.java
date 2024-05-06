package com.seyed.ali.authenticationservice.keycloak.service;

import com.seyed.ali.authenticationservice.keycloak.model.dto.RoleDTO;
import com.seyed.ali.authenticationservice.keycloak.model.dto.UserDTO;
import com.seyed.ali.authenticationservice.keycloak.util.KeycloakSecurityUtil;
import com.seyed.ali.authenticationservice.keycloak.util.converter.RoleDTOToRoleRepresentationConverter;
import com.seyed.ali.authenticationservice.keycloak.util.converter.RoleRepresentationToRoleDTOConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.*;
import org.keycloak.representations.idm.RoleRepresentation;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KeycloakAdminRoleServiceImplTest {

    //<editor-fold desc="fields">
    private @InjectMocks KeycloakAdminRoleServiceImpl keycloakAdminRoleService;
    private @Mock Keycloak keycloak;
    private @Mock KeycloakSecurityUtil keycloakSecurityUtil;
    private @Mock UsersResource usersResource;
    private @Mock RolesResource rolesResource;
    private @Mock UserDTO userDTO;
    private @Mock RoleDTO roleDTO;
    private @Mock RoleDTOToRoleRepresentationConverter roleDTOToRoleRepresentationConverter;
    private @Mock RoleRepresentationToRoleDTOConverter roleRepresentationToRoleDTOConverter;
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
        this.roleDTO = new RoleDTO("Some_Role", "Some_long_description");

        // Initialize the keycloak mock
        // mocking `KeycloakSecurityUtil#createOrGetKeycloakInstance()`
        this.keycloak = mock(Keycloak.class);
        when(this.keycloakSecurityUtil.createOrGetKeycloakInstance())
                .thenReturn(this.keycloak);

        // we're adding this code in each method; because some stubbing is not necessary for other methods, while others are -> which  leads to stubbing error
        /*RealmResource realmResource = mock(RealmResource.class);

        // mocking `Keycloak#realm(String)`
        when(this.keycloak.realm(null))
                .thenReturn(realmResource);

        // mocking `RealmResource#users()`
        when(realmResource.users())
                .thenReturn(this.usersResource);

        // mocking `RealmResource#roles()`
        when(realmResource.roles())
                .thenReturn(this.rolesResource);*/
    }
    //</editor-fold>

    @Test
    void getRolesList() {
        // given
        RealmResource realmResource = mock(RealmResource.class);
        // mocking `Keycloak#realm(String)`
        when(this.keycloak.realm(null))
                .thenReturn(realmResource);

        // mocking `RealmResource#roles()`
        when(realmResource.roles())
                .thenReturn(this.rolesResource);

        // mocking `RoleRepresentationToRoleDTOConverter#convert(RoleRepresentation)`
        when(this.roleRepresentationToRoleDTOConverter.convert(isA(RoleRepresentation.class)))
                .thenReturn(this.roleDTO);

        // mocking `RolesResource#list()`
        RoleRepresentation roleRepresentation = mock(RoleRepresentation.class);
        when(this.rolesResource.list())
                .thenReturn(List.of(roleRepresentation));

        // when
        List<RoleDTO> rolesList_actualResponse = this.keycloakAdminRoleService.getRolesList();

        // then
        assertThat(rolesList_actualResponse)
                .as("Must not be empty")
                .isNotEmpty()
                .as("Must have 1 data")
                .hasSize(1);
    }

    @Test
    void getSingleRoleRepresentation() {
        // given
        String someId = "some_id";
        RealmResource realmResource = mock(RealmResource.class);
        // mocking `Keycloak#realm(String)`
        when(this.keycloak.realm(null))
                .thenReturn(realmResource);

        // mocking `RealmResource#roles()`
        when(realmResource.roles())
                .thenReturn(this.rolesResource);

        // mocking `RolesResource#get(String)`
        RoleResource roleResource = mock(RoleResource.class);
        when(this.rolesResource.get(isA(String.class)))
                .thenReturn(roleResource);

        // mocking `RoleResource#toRepresentation()`
        RoleRepresentation roleRepresentation = mock(RoleRepresentation.class);
        when(roleResource.toRepresentation())
                .thenReturn(roleRepresentation);

        // mocking `RoleRepresentationToRoleDTOConverter.#convert(RoleRepresentation)`
        when(this.roleRepresentationToRoleDTOConverter.convert(isA(RoleRepresentation.class)))
                .thenReturn(this.roleDTO);

        // when
        RoleDTO singleRoleRepresentation_actualResponse = this.keycloakAdminRoleService.getSingleRoleRepresentation(someId);

        // then
        assertThat(singleRoleRepresentation_actualResponse)
                .as("Must not be empty")
                .isNotNull()
                .as("Must have same data")
                .isEqualTo(this.roleDTO);
    }

    @Test
    void createRoleRepresentation() {
        // given
        // mocking `RoleDTOToRoleRepresentationConverter#convert(RoleDTO)`
        RoleRepresentation roleRepresentation = mock(RoleRepresentation.class);
        when(this.roleDTOToRoleRepresentationConverter.convert(isA(RoleDTO.class)))
                .thenReturn(roleRepresentation);

        RealmResource realmResource = mock(RealmResource.class);
        // mocking `Keycloak#realm(String)`
        when(this.keycloak.realm(null))
                .thenReturn(realmResource);

        // mocking `RealmResource#roles()`
        when(realmResource.roles())
                .thenReturn(this.rolesResource);

        // mocking `RolesResource#create(RoleRepresentation)`
        doNothing()
                .when(this.rolesResource)
                .create(isA(RoleRepresentation.class));


        // when
        RoleRepresentation singleRoleRepresentation_actualResponse = this.keycloakAdminRoleService.createRoleRepresentation(this.roleDTO);

        // then
        assertThat(singleRoleRepresentation_actualResponse)
                .as("Must not be empty")
                .isNotNull()
                .as("Must have same data")
                .isEqualTo(roleRepresentation);
    }

    @Test
    void updateRoleName() {
        // given
        // mocking `RoleDTOToRoleRepresentationConverter#convert(RoleDTO)`
        RoleRepresentation roleRepresentation = mock(RoleRepresentation.class);
        when(this.roleDTOToRoleRepresentationConverter.convert(isA(RoleDTO.class)))
                .thenReturn(roleRepresentation);

        RealmResource realmResource = mock(RealmResource.class);
        // mocking `Keycloak#realm(String)`
        when(this.keycloak.realm(null))
                .thenReturn(realmResource);

        // mocking `RealmResource#roles()`
        when(realmResource.roles())
                .thenReturn(this.rolesResource);

        // mocking `RolesResource#get(String)`
        RoleResource roleResource = mock(RoleResource.class);
        when(this.rolesResource.get(isA(String.class)))
                .thenReturn(roleResource);

        // mocking `RoleResource#update(RoleRepresentation)`
        doNothing()
                .when(roleResource)
                .update(roleRepresentation);

        // when
        this.keycloakAdminRoleService.updateRoleName(this.roleDTO);

        // then
        verify(this.roleDTOToRoleRepresentationConverter, times(1))
                .convert(isA(RoleDTO.class));
        verify(roleResource, times(1).description("Must have 1 interaction"))
                .update(roleRepresentation);
    }

    @Test
    void deleteRole() {
        // given
        RealmResource realmResource = mock(RealmResource.class);
        // mocking `Keycloak#realm(String)`
        when(this.keycloak.realm(null))
                .thenReturn(realmResource);

        // mocking `RealmResource#roles()`
        when(realmResource.roles())
                .thenReturn(this.rolesResource);

        // mocking `RolesResource#get(String)`
        RoleResource roleResource = mock(RoleResource.class);
        doNothing()
                .when(this.rolesResource)
                .deleteRole(isA(String.class));

        // when
        this.keycloakAdminRoleService.deleteRole(this.roleDTO.name());

        // then
        verify(this.rolesResource, times(1))
                .deleteRole(this.roleDTO.name());
    }

    @Test
    void getUserRoles() {
        // given
        RealmResource realmResource = mock(RealmResource.class);
        // mocking `Keycloak#realm(String)`
        when(this.keycloak.realm(null))
                .thenReturn(realmResource);

        // mocking `RealmResource#users()`
        when(realmResource.users())
                .thenReturn(this.usersResource);

        // mocking `UsersResource#get(String)`
        UserResource userResource = mock(UserResource.class);
        when(this.usersResource.get(isA(String.class)))
                .thenReturn(userResource);

        // mocking `UserResource#roles()`
        RoleMappingResource roleMappingResource = mock(RoleMappingResource.class);
        when(userResource.roles())
                .thenReturn(roleMappingResource);

        // mocking `RoleMappingResource#realmLevel()`
        RoleScopeResource roleScopeResource = mock(RoleScopeResource.class);
        when(roleMappingResource.realmLevel())
                .thenReturn(roleScopeResource);

        // mocking `RoleScopeResource#listAll()`
        RoleRepresentation roleRepresentation = mock(RoleRepresentation.class);
        when(roleScopeResource.listAll())
                .thenReturn(List.of(roleRepresentation));

        // mocking `RoleRepresentationToRoleDTOConverter#convert(RoleRepresentation)`
        when(this.roleRepresentationToRoleDTOConverter.convert(isA(RoleRepresentation.class)))
                .thenReturn(this.roleDTO);

        // when
        List<RoleDTO> rolesList_actualResponse = this.keycloakAdminRoleService.getUserRoles(this.userDTO.id());

        // then
        assertThat(rolesList_actualResponse)
                .as("Must not be empty")
                .isNotEmpty()
                .as("Must have 1 data")
                .hasSize(1);
    }

    @Test
    void addRoleToUser() {
        // given
        String userId = "userId";
        String roleName = "som_role";

        RealmResource realmResource = mock(RealmResource.class);

        // mocking `Keycloak#realm(String)`
        when(this.keycloak.realm(null))
                .thenReturn(realmResource);

        // mocking `RealmResource#users()`
        when(realmResource.users())
                .thenReturn(this.usersResource);

        // mocking `RealmResource#roles()`
        when(realmResource.roles())
                .thenReturn(this.rolesResource);

        RoleResource roleResource = mock(RoleResource.class);
        RoleRepresentation roleRepresentation = mock(RoleRepresentation.class);

        // mocking `RolesResource#toRepresentation()`
        when(roleResource.toRepresentation())
                .thenReturn(roleRepresentation);

        // mocking `RolesResource#get(String)`
        when(this.rolesResource.get(isA(String.class)))
                .thenReturn(roleResource);

        // mocking ``
        UserResource userResource = mock(UserResource.class);
        when(this.usersResource.get(isA(String.class)))
                .thenReturn(userResource);

        // Mocking `UserResource`
        when(this.usersResource.get(userId))
                .thenReturn(userResource);

        // Mocking `RoleMappingResource`
        RoleMappingResource roleMappingResource = mock(RoleMappingResource.class);
        RoleScopeResource roleScopeResource = mock(RoleScopeResource.class);

        // mocking `UserResource#roles()`
        when(userResource.roles())
                .thenReturn(roleMappingResource);

        // mocking ``
        when(userResource.roles().realmLevel())
                .thenReturn(roleScopeResource);

        // when
        this.keycloakAdminRoleService.addRoleToUser(userId, roleName);

        // then
        verify(roleScopeResource, times(1))
                .add(List.of(roleRepresentation));
    }

    @Test
    void testAddRoleToUser() throws NoSuchFieldException, IllegalAccessException {
        // given
        String defaultUserRole = "som_role";

        RealmResource realmResource = mock(RealmResource.class);

        // mocking `Keycloak#realm(String)`
        when(this.keycloak.realm(null))
                .thenReturn(realmResource);

        // mocking `RealmResource#roles()`
        when(realmResource.roles())
                .thenReturn(this.rolesResource);

        // mocking `RolesResource#get(String)`
        Field defaultUserRoleField = KeycloakAdminRoleServiceImpl.class
                .getDeclaredField("defaultUserRole");
        defaultUserRoleField.setAccessible(true);
        defaultUserRoleField.set(keycloakAdminRoleService, defaultUserRole);

        RoleResource roleResource = mock(RoleResource.class);
        when(this.rolesResource.get(defaultUserRole))
                .thenReturn(roleResource);

        // mocking `RolesResource#get(String)`
        RoleRepresentation roleRepresentation = mock(RoleRepresentation.class);
        when(roleResource.toRepresentation())
                .thenReturn(roleRepresentation);

        // mocking `RealmResource#users()`
        when(realmResource.users())
                .thenReturn(this.usersResource);

        // mocking `UsersResource#get(String)`
        UserResource userResource = mock(UserResource.class);
        when(this.usersResource.get(isA(String.class)))
                .thenReturn(userResource);

        // mocking `UserResource#roles()`
        RoleMappingResource roleMappingResource = mock(RoleMappingResource.class);
        when(userResource.roles())
                .thenReturn(roleMappingResource);

        // mocking `RoleMappingResource#realmLevel()`
        RoleScopeResource roleScopeResource = mock(RoleScopeResource.class);
        when(roleMappingResource.realmLevel())
                .thenReturn(roleScopeResource);

        // when
        this.keycloakAdminRoleService.addRoleToUser(this.userDTO.id());

        // then
        verify(roleScopeResource, times(1).description("Must hit once"))
                .add(List.of(roleRepresentation));
    }

    @Test
    void removeUsersRole() {
        // given
        RealmResource realmResource = mock(RealmResource.class);

        // mocking `Keycloak#realm(String)`
        when(this.keycloak.realm(null))
                .thenReturn(realmResource);

        // mocking `RealmResource#roles()`
        when(realmResource.roles())
                .thenReturn(this.rolesResource);

        // mocking `RolesResource#get(String)`
        RoleResource roleResource = mock(RoleResource.class);
        when(this.rolesResource.get(isA(String.class)))
                .thenReturn(roleResource);

        // mocking `RolesResource#get(String)`
        RoleRepresentation roleRepresentation = mock(RoleRepresentation.class);
        when(roleResource.toRepresentation())
                .thenReturn(roleRepresentation);

        // mocking `RealmResource#users()`
        when(realmResource.users())
                .thenReturn(this.usersResource);

        // mocking `UsersResource#get(String)`
        UserResource userResource = mock(UserResource.class);
        when(this.usersResource.get(isA(String.class)))
                .thenReturn(userResource);

        // mocking `UserResource#roles()`
        RoleMappingResource roleMappingResource = mock(RoleMappingResource.class);
        when(userResource.roles())
                .thenReturn(roleMappingResource);

        // mocking `RoleMappingResource#realmLevel()`
        RoleScopeResource roleScopeResource = mock(RoleScopeResource.class);
        when(roleMappingResource.realmLevel())
                .thenReturn(roleScopeResource);

        // when
        this.keycloakAdminRoleService.removeUsersRole(this.userDTO.id(), this.roleDTO.name());

        // then
        verify(roleScopeResource, times(1).description("Must hit once"))
                .remove(List.of(roleRepresentation));
    }

}