package com.seyed.ali.authenticationservice.keycloak.repository;

import com.seyed.ali.authenticationservice.keycloak.model.domain.KeycloakUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KeycloakUserRepository extends JpaRepository<KeycloakUser, String> {
}