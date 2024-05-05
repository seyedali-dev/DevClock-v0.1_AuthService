package com.seyed.ali.authenticationservice.keycloak.service.interfaces;

public interface KeycloakAdminRoleService {

    void addRoleToUser(String userId, String roleName);

}
