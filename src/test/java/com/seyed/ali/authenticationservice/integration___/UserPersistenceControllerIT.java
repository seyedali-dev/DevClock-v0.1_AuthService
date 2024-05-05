package com.seyed.ali.authenticationservice.integration___;

import com.seyed.ali.authenticationservice.config.EurekaClientTestConfiguration;
import com.seyed.ali.authenticationservice.keycloak.model.domain.KeycloakRoles;
import com.seyed.ali.authenticationservice.keycloak.model.domain.KeycloakUser;
import com.seyed.ali.authenticationservice.keycloak.service.interfaces.UserPersistenceService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Set;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest({"server.port=0"}) /* random port: `webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT` fails while trying to run the test */
@EnableConfigurationProperties /* to use application.yml-test file */
@ActiveProfiles("test")
@AutoConfigureMockMvc/* calling the api itself */
@ContextConfiguration(classes = {EurekaClientTestConfiguration.class}) /* to call the configuration in the test (for service-registry configs) */
class UserPersistenceControllerIT {

    //<editor-fold desc="fields">
    private @MockBean UserPersistenceService userPersistenceService;

    private @Autowired MockMvc mockMvc;
    //</editor-fold>

    @Test
    void handleUser() throws Exception {
        // given
        String someAuthority = "any_role";

        KeycloakUser keycloakUser = new KeycloakUser();
        keycloakUser.setId("subject!");
        keycloakUser.setEmail("some@email.com");
        keycloakUser.setKeycloakRoles(Set.of(
                new KeycloakRoles("1", someAuthority, Set.of(keycloakUser))
        ));

        when(userPersistenceService.handleUser(any(Jwt.class)))
                .thenReturn(keycloakUser);

        // when
        this.mockMvc.perform(
                        MockMvcRequestBuilders.post("/keycloak-user/handle-user")
                                .with(jwt().authorities(new SimpleGrantedAuthority(someAuthority)))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(keycloakUser.getId())))
                .andExpect(jsonPath("$.email", is(keycloakUser.getEmail())))
                .andExpect(jsonPath("$.keycloakRoles").isNotEmpty());

        // then
        Mockito.verify(this.userPersistenceService, times(1))
                .handleUser(any(Jwt.class));
    }

}