package com.seyed.ali.authenticationservice.keycloak.controller;

import com.seyed.ali.authenticationservice.keycloak.model.domain.KeycloakUser;
import com.seyed.ali.authenticationservice.keycloak.service.interfaces.UserPersistenceService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//TODO: Implement API-Gateway

/**
 * This class's endpoints are to be used in <code>API-Gateway</code>.
 * <p>
 * Since we're using keycloak, when a user logs in using keycloak, our own database, won't know of that user
 * since we don't have any data of him.
 * <p>
 * So what we can do is, intercept each request, and look for that user in the database. I know that this will impact
 * the performance. For that, I'll implement caching with Redis later on.
 * <p>
 * So using <code>API-Gateway</code>, we intercept each request, and execute the {@link UserPersistenceController#handleUser(Jwt)}
 * method to check whether that user is present or not. If not present, save him in the db.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/keycloak-user")
@SecurityRequirement(name = "Keycloak")
@Tag(name = "Keycloak User", description = "Registering new users when they authenticate themselves using Keycloak.")
public class UserPersistenceController {

    private final UserPersistenceService userPersistenceService;

    @PostMapping("/handle-user")
    public ResponseEntity<KeycloakUser> handleUser(@AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(this.userPersistenceService.handleUser(jwt));
    }

}
