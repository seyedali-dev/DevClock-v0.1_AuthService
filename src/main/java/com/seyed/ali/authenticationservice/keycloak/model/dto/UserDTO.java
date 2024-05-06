package com.seyed.ali.authenticationservice.keycloak.model.dto;

import lombok.Builder;

@Builder
public record UserDTO(String id,
                      String firstName,
                      String lastName,
                      String email,
                      String username,
                      String password) {
}
