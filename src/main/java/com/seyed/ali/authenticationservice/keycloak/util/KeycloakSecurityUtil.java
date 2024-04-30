package com.seyed.ali.authenticationservice.keycloak.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Utility class for security-related information from Keycloak JWT tokens.
 */
@Component
@RequiredArgsConstructor
public class KeycloakSecurityUtil {

    /**
     * Object mapper instance for converting JSON data to Java objects.
     */
    private final ObjectMapper objectMapper;

    /**
     * Extracts a collection of granted authorities from a Keycloak JWT token.
     * <p>
     * In Keycloak JWT token, the roles are present in the {@code realm_access} field. We're extracting the roles,
     * and mapping it to a {@link SimpleGrantedAuthority}.
     * <p>
     * Look into the tests!
     *
     * @param jwt the Keycloak JWT token
     * @return a collection of granted authorities
     */
    public Collection<GrantedAuthority> extractAuthorities(Jwt jwt) {
        // Get the "realm_access" claim from the JWT token
        Map<String, Object> realmAccess = jwt.getClaim("realm_access");

        // If the claim is null, return an empty list of authorities
        if (realmAccess == null) {
            return new ArrayList<>();
        }

        // Extract the roles from the "realm_access" claim
        Collection<String> roles = this.objectMapper.convertValue(realmAccess.get("roles"), Collection.class);

        // Create a list of granted authorities from the roles
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String role : roles) {
            authorities.add(new SimpleGrantedAuthority(role));
        }

        return authorities;
    }

}
