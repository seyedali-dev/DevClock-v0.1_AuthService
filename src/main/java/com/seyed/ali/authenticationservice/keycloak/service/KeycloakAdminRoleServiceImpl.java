package com.seyed.ali.authenticationservice.keycloak.service;

import com.seyed.ali.authenticationservice.keycloak.service.interfaces.KeycloakAdminRoleService;
import com.seyed.ali.authenticationservice.keycloak.util.KeycloakSecurityUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.RoleRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KeycloakAdminRoleServiceImpl implements KeycloakAdminRoleService {

    private @Value("${keycloak.user.realm}") String realm;

    private final Keycloak keycloak;
    private final KeycloakSecurityUtil keycloakSecurityUtil;

    @Autowired
    public KeycloakAdminRoleServiceImpl(KeycloakSecurityUtil keycloakSecurityUtil) {
        this.keycloakSecurityUtil = keycloakSecurityUtil;
        this.keycloak = this.keycloakSecurityUtil.createOrGetKeycloakInstance();
    }


    @Override
    public void addRoleToUser(String userId, String roleName) {
        RoleRepresentation roleRepresentation = this.keycloak.realm(this.realm)
                .roles()
                .get(roleName)
                .toRepresentation();

        this.keycloak.realm(this.realm)
                .users()
                .get(userId)
                .roles()
                .realmLevel()
                .add(List.of(roleRepresentation));
    }

}
