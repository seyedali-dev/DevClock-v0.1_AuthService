package com.seyed.ali.authenticationservice.keycloak.service;

import com.seyed.ali.authenticationservice.keycloak.model.dto.RoleDTO;
import com.seyed.ali.authenticationservice.keycloak.service.interfaces.KeycloakAdminRoleService;
import com.seyed.ali.authenticationservice.keycloak.util.KeycloakSecurityUtil;
import com.seyed.ali.authenticationservice.keycloak.util.converter.RoleDTOToRoleRepresentationConverter;
import com.seyed.ali.authenticationservice.keycloak.util.converter.RoleRepresentationToRoleDTOConverter;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RoleScopeResource;
import org.keycloak.admin.client.resource.RolesResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.common.util.CollectionUtil;
import org.keycloak.representations.idm.RoleRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class KeycloakAdminRoleServiceImpl implements KeycloakAdminRoleService {

    private @Value("${keycloak.user.realm}") String realm;
    private @Value("${keycloak.user.default-role}") String defaultUserRole;

    private Keycloak keycloak;
    private final KeycloakSecurityUtil keycloakSecurityUtil;
    private final RoleDTOToRoleRepresentationConverter roleDTOToRoleRepresentationConverter;
    private final RoleRepresentationToRoleDTOConverter roleRepresentationToRoleDTOConverter;

    @Autowired
    public KeycloakAdminRoleServiceImpl(
            KeycloakSecurityUtil keycloakSecurityUtil,
            RoleDTOToRoleRepresentationConverter roleDTOToRoleRepresentationConverter,
            RoleRepresentationToRoleDTOConverter roleRepresentationToRoleDTOConverter
    ) {
        this.keycloakSecurityUtil = keycloakSecurityUtil;
        this.roleDTOToRoleRepresentationConverter = roleDTOToRoleRepresentationConverter;
        this.roleRepresentationToRoleDTOConverter = roleRepresentationToRoleDTOConverter;
    }

    // TODO: update the javadoc; last 4 lines have problem.

    /**
     * This method is a lazy loader for the {@link Keycloak} instance. It checks if a Keycloak instance has already been retrieved from {@link KeycloakSecurityUtil}, and if not, it retrieves it.
     * <p>
     * This method ensures that the Keycloak instance is retrieved only when it’s needed, and it’s retrieved only once per {@link KeycloakAdminUserServiceImpl} instance.
     * <p><br>
     * The issue is that the Keycloak instance is being initialized in the constructor of KeycloakAdminUserServiceImpl.
     * This means that when your tests run, the Keycloak instance is already null because the setUp() method (which mocks the Keycloak instance) hasn’t been called yet.
     * <p>
     * One way to solve this is to refactor your KeycloakAdminUserServiceImpl to lazily load the Keycloak instance.
     * This means that the Keycloak instance won’t be loaded until it’s actually needed.
     *
     * @return
     */
    private Keycloak lazyLoadKeycloakInstance() {
        if (this.keycloak == null) {
            this.keycloak = this.keycloakSecurityUtil.createOrGetKeycloakInstance();
        }
        return this.keycloak;
    }

    private RolesResource getRolesResource() {
        return this.lazyLoadKeycloakInstance()
                .realm(this.realm)
                .roles();
    }

    private UsersResource getUsersResource() {
        return this.lazyLoadKeycloakInstance()
                .realm(this.realm)
                .users();
    }

    //---===============================================================-->

    @Override
    public List<RoleDTO> getRolesList() {
        List<RoleDTO> roles = new ArrayList<>();

        List<RoleRepresentation> roleRepresentationList = this.getRolesResource().list();
        if (CollectionUtil.isNotEmpty(roleRepresentationList))
            roleRepresentationList.forEach(roleRepresentation -> roles.add(this.roleRepresentationToRoleDTOConverter.convert(roleRepresentation)));
        return roles;
    }

    @Override
    public RoleDTO getSingleRoleRepresentation(String roleName) {
        RoleRepresentation roleRepresentation = this.getRolesResource().get(roleName).toRepresentation();
        return this.roleRepresentationToRoleDTOConverter.convert(roleRepresentation);
    }

    @Override
    public RoleRepresentation createRoleRepresentation(RoleDTO roleDTO) {
        RoleRepresentation roleRepresentation = this.roleDTOToRoleRepresentationConverter.convert(roleDTO);
        this.getRolesResource().create(roleRepresentation);
        return roleRepresentation;
    }

    @Override
    public void updateRoleName(RoleDTO roleDTO) {
        RoleRepresentation roleRepresentation = this.roleDTOToRoleRepresentationConverter.convert(roleDTO);
        this.getRolesResource().get(roleDTO.name()).update(roleRepresentation);
    }

    @Override
    public void deleteRole(String roleName) {
        this.getRolesResource().deleteRole(roleName);
    }

    @Override
    public List<RoleDTO> getUserRoles(String id) {
        List<RoleDTO> userRoleDTOs = new ArrayList<>();

        this.getUsersResource()
                .get(id)
                .roles()
                .realmLevel()
                .listAll()
                .forEach(roleRepresentation -> userRoleDTOs.add(this.roleRepresentationToRoleDTOConverter.convert(roleRepresentation)));
        return userRoleDTOs;
    }

    @Override
    public void addRoleToUser(String userId, String roleName) {
        RoleRepresentation roleRepresentation = this.getRolesResource()
                .get(roleName)
                .toRepresentation();

        getUsersResource()
                .get(userId)
                .roles()
                .realmLevel()
                .add(List.of(roleRepresentation));
    }

    @Override
    public void addRoleToUser(String id) {
        RoleRepresentation roleRepresentation = this.getRolesResource()
                .get(this.defaultUserRole)
                .toRepresentation();

        RoleScopeResource roleScopeResource = this.getUsersResource()
                .get(id)
                .roles()
                .realmLevel();
        roleScopeResource.add(List.of(roleRepresentation));
    }

    @Override
    public void removeUsersRole(String id, String roleName) {
        RoleRepresentation roleRepresentation = this.getRolesResource()
                .get(roleName)
                .toRepresentation();
        this.getUsersResource()
                .get(id)
                .roles()
                .realmLevel()
                .remove(List.of(roleRepresentation));
    }

}
