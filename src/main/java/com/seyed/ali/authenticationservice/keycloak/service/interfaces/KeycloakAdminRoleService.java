package com.seyed.ali.authenticationservice.keycloak.service.interfaces;

import com.seyed.ali.authenticationservice.keycloak.model.dto.RoleDTO;
import org.keycloak.representations.idm.RoleRepresentation;

import java.util.List;

public interface KeycloakAdminRoleService {

    List<RoleDTO> getRolesList();

    RoleDTO getSingleRoleRepresentation(String roleName);

    RoleRepresentation createRoleRepresentation(RoleDTO roleDTO);

    void updateRoleName(RoleDTO roleDTO);

    void deleteRole(String roleName);

    List<RoleDTO> getUserRoles(String id);

    void addRoleToUser(String userId, String roleName);

    void addRoleToUser(String id);

    void removeUsersRole(String id, String roleName);

}
