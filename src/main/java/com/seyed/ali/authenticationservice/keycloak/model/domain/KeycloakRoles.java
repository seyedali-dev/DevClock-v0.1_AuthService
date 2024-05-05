package com.seyed.ali.authenticationservice.keycloak.model.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class KeycloakRoles implements Serializable {

    @Id
    private String roleId;

    private String roleName;

    @ManyToMany(mappedBy = "keycloakRoles")
    @JsonIgnore
    private Set<KeycloakUser> keycloakUser = new HashSet<>();

}
