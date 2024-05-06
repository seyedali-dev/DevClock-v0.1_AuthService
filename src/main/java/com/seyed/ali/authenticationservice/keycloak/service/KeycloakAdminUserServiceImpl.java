package com.seyed.ali.authenticationservice.keycloak.service;

import com.seyed.ali.authenticationservice.keycloak.model.dto.UserDTO;
import com.seyed.ali.authenticationservice.keycloak.service.interfaces.KeycloakAdminUserService;
import com.seyed.ali.authenticationservice.keycloak.util.KeycloakSecurityUtil;
import com.seyed.ali.authenticationservice.keycloak.util.converter.UserDTOToUserRepresentationConverter;
import com.seyed.ali.authenticationservice.keycloak.util.converter.UserRepresentationToUserDtoConverter;
import jakarta.ws.rs.core.Response;
import org.apache.commons.collections.CollectionUtils;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class KeycloakAdminUserServiceImpl implements KeycloakAdminUserService {

    private @Value("${keycloak.user.realm}") String realm;

    private final UserRepresentationToUserDtoConverter userRepresentationToUserDtoConverter;
    private final UserDTOToUserRepresentationConverter userDTOToUserRepresentationConverter;
    private final KeycloakSecurityUtil keycloakSecurityUtil;
    private Keycloak keycloak;

    public KeycloakAdminUserServiceImpl(
            UserRepresentationToUserDtoConverter userRepresentationToUserDtoConverter,
            UserDTOToUserRepresentationConverter userDTOToUserRepresentationConverter,
            KeycloakSecurityUtil keycloakSecurityUtil
    ) {
        this.userRepresentationToUserDtoConverter = userRepresentationToUserDtoConverter;
        this.userDTOToUserRepresentationConverter = userDTOToUserRepresentationConverter;
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

    private UsersResource getUsersResource() {
        return this.lazyLoadKeycloakInstance()
                .realm(this.realm)
                .users();
    }

    //---===============================================================-->

    @Override
    public List<UserDTO> getUserDTOList() {
        List<UserRepresentation> userRepresentationList = getUsersResource()
                .list();
        List<UserDTO> userDTOList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(userRepresentationList)) {
            userRepresentationList.forEach(userRepresentation -> userDTOList.add(this.userRepresentationToUserDtoConverter.convert(userRepresentation)));
        }
        return userDTOList;
    }

    @Override
    public UserDTO getSingleUserDTO(String id) {
        UserRepresentation foundUserRepresentation = getUsersResource()
                .get(id)
                .toRepresentation();
        return this.userRepresentationToUserDtoConverter.convert(foundUserRepresentation);
    }

    @Override
    public Map<String, String> createUserRepresentation(UserDTO userDTO) {
        UserRepresentation userRepresentation = this.userDTOToUserRepresentationConverter.convert(userDTO);
        Response response = this.getUsersResource().create(userRepresentation);
        Map<String, String> responseMap = new HashMap<>();

        // extract user id from the created path: .../some_user_id --> some_user_id will be the response
        if (response.getStatus() == Response.Status.CREATED.getStatusCode()) {
            URI location = response.getLocation();
            String[] splitPath = location.getPath().split("/");
            String userId = splitPath[splitPath.length - 1];

            responseMap.put("message", "User created!");
            responseMap.put("userId", userId);
        } else responseMap.put("message", "Could not create the user :( please see the logs.");

        return responseMap;
    }

    @Override
    public Map<String, String> updateUserRepresentation(String userId, UserDTO userDto) {
        UserRepresentation userRepresentation = this.userDTOToUserRepresentationConverter.convert(userDto);
        this.getUsersResource().get(userId).update(userRepresentation);
        return Map.of(
                "message", "User updated!",
                "userId", userId
        );
    }

    @Override
    public void deleteUserRepresentation(String id) {
        this.getUsersResource().delete(id);
    }

}

