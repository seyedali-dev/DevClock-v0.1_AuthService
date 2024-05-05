package com.seyed.ali.authenticationservice.keycloak.repository;

import com.seyed.ali.authenticationservice.keycloak.model.domain.KeycloakRoles;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface KeycloakRolesRepository extends JpaRepository<KeycloakRoles, String> {

    Optional<KeycloakRoles> findByRoleName(String roleName);

}