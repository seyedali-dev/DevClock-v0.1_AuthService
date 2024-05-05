package com.seyed.ali.authenticationservice.keycloak.service;

import com.seyed.ali.authenticationservice.keycloak.model.dto.UserDTO;
import com.seyed.ali.authenticationservice.keycloak.service.interfaces.KeycloakAdminUserService;
import com.seyed.ali.authenticationservice.keycloak.util.KeycloakSecurityUtil;
import com.seyed.ali.authenticationservice.keycloak.util.converter.UserRepresentationToUserDtoConverter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class KeycloakAdminUserServiceImpl implements KeycloakAdminUserService {

    private @Value("${keycloak.user.realm}") String realm;

    private final UserRepresentationToUserDtoConverter userRepresentationToUserDtoConverter;
    private final KeycloakSecurityUtil keycloakSecurityUtil;
    private Keycloak keycloak;

    public KeycloakAdminUserServiceImpl(
            UserRepresentationToUserDtoConverter userRepresentationToUserDtoConverter,
            KeycloakSecurityUtil keycloakSecurityUtil
    ) {
        this.userRepresentationToUserDtoConverter = userRepresentationToUserDtoConverter;
        this.keycloakSecurityUtil = keycloakSecurityUtil;
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

    @Override
    public List<UserDTO> getUserDTOList() {
        List<UserRepresentation> userRepresentationList = this.lazyLoadKeycloakInstance().realm(this.realm)
                .users()
                .list();
        List<UserDTO> userDTOList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(userRepresentationList)) {
            userRepresentationList.forEach(userRepresentation -> userDTOList.add(this.userRepresentationToUserDtoConverter.convert(userRepresentation)));
        }
        return userDTOList;
    }

}

