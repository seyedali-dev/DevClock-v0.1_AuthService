package com.seyed.ali.authenticationservice.keycloak.service.interfaces;

import com.seyed.ali.authenticationservice.keycloak.model.dto.UserDTO;

import java.util.List;

public interface KeycloakAdminUserService {

    List<UserDTO> getUserDTOList();

}
