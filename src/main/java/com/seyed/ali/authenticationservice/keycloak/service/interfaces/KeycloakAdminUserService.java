package com.seyed.ali.authenticationservice.keycloak.service.interfaces;

import com.seyed.ali.authenticationservice.keycloak.model.dto.UserDTO;

import java.util.List;
import java.util.Map;

public interface KeycloakAdminUserService {

    List<UserDTO> getUserDTOList();

    UserDTO getSingleUserDTO(String id);

    Map<String, String> createUserRepresentation(UserDTO userDTO);

}
