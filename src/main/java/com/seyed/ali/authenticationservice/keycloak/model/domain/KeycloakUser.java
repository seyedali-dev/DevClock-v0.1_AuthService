package com.seyed.ali.authenticationservice.keycloak.model.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
public class KeycloakUser {

    @Id
    @Column(unique = true, nullable = false, updatable = false, name = "subject")
    private String id;

    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private boolean emailVerified;
    private String phoneNumber;
    private String address;
    private String zoneInfo;
    private LocalDateTime birthDate;

    @ManyToMany
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "subject_user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    @ToString.Exclude
    private Set<KeycloakRoles> keycloakRoles = new HashSet<>();


}
