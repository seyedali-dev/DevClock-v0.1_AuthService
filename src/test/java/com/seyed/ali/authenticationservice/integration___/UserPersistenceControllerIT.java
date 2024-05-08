//package com.seyed.ali.authenticationservice.integration___;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.seyed.ali.authenticationservice.config.EurekaClientTestConfiguration;
//import com.seyed.ali.authenticationservice.integration___.keycloak.AccessTokenProvider;
//import com.seyed.ali.authenticationservice.keycloak.model.domain.KeycloakRoles;
//import com.seyed.ali.authenticationservice.keycloak.model.domain.KeycloakUser;
//import dasniko.testcontainers.keycloak.KeycloakContainer;
//import org.apache.http.HttpHeaders;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.context.properties.EnableConfigurationProperties;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.DynamicPropertyRegistry;
//import org.springframework.test.context.DynamicPropertySource;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.ResultActions;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.testcontainers.junit.jupiter.Container;
//import org.testcontainers.junit.jupiter.Testcontainers;
//
//import java.util.Map;
//import java.util.Set;
//
//import static io.restassured.RestAssured.given;
//import static org.hamcrest.Matchers.is;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@SuppressWarnings("preview")
//@SpringBootTest({"server.port=0"}) /* random port: `webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT` fails while trying to run the test */
//@EnableConfigurationProperties /* to use application.yml-test file */
//@ActiveProfiles("test")
//@AutoConfigureMockMvc/* calling the api itself */
//@ContextConfiguration(classes = {EurekaClientTestConfiguration.class}) /* to call the configuration in the test (for service-registry configs) */
//
//@Testcontainers
//class UserPersistenceController2IT {
//
//    //<editor-fold desc="fields">
//    @Container
//    static KeycloakContainer keycloakContainer = new KeycloakContainer("quay.io/keycloak/keycloak:23.0.7")
//            .withRealmImportFiles("/test-realm/DevVault-v2.0-test-realm.json");
//
//    private @Autowired MockMvc mockMvc;
//    private @Autowired ObjectMapper objectMapper;
//    //</editor-fold>
//
//    @DynamicPropertySource
//    public static void setProperties(DynamicPropertyRegistry propertyRegistry) {
//        propertyRegistry.add(
//                "spring.security.oauth2.resourceserver.jwt.issuer-uri",
//                () -> keycloakContainer.getAuthServerUrl() + "/realms/DevVault-v2.0"
//        );
//        propertyRegistry.add(
//                "spring.security.oauth2.resourceserver.jwt.jwk-set-uri",
//                () -> keycloakContainer.getAuthServerUrl() + "/realms/DevVault-v2.0/protocol/openid-connect/certs"
//        );
//    }
//
//    @Test
//    void handleUser() throws Exception {
//        // given
//        String someAuthority = "any_role";
//
//        KeycloakUser keycloakUser = new KeycloakUser();
//        keycloakUser.setId("subject!");
//        keycloakUser.setEmail("some@email.com");
//        keycloakUser.setKeycloakRoles(Set.of(
//                new KeycloakRoles("1", someAuthority, Set.of(keycloakUser))
//        ));
//
//        String json = this.objectMapper.writeValueAsString(keycloakUser);
//
//        String CLIENT_ID = "DevVault-v2.0"; // note that, the tests will fail if we change `clientId` field in the realm import json file.
//        String CLIENT_SECRET = "some_client_secret"; // we can change it to whatever we want, it's fine.
//        String generateTokenUrl = STR."\{keycloakContainer.getAuthServerUrl()}/realms/DevVault-v2.0/protocol/openid-connect/token";
//        System.out.println("Generated token URL: " + generateTokenUrl);
//        String accessToken = given()
//                .contentType("application/x-www-form-urlencoded")
//                .formParams(Map.of(
//                        "username", "default",
//                        "password", "1",
//                        "grant_type", "password",
//                        "client_id", CLIENT_ID,
//                        "client_secret", CLIENT_SECRET
//                ))
//                .post(generateTokenUrl)
//                .then()
//                .assertThat().statusCode(200).onFailMessage("Must be success")
//                .extract()
//                .path("access_token");
//
//        // when
//        ResultActions resultActions = this.mockMvc.perform(
//                MockMvcRequestBuilders.post("/keycloak-user/handle-user")
//                        .header(HttpHeaders.AUTHORIZATION, STR."Bearer \{accessToken}")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(json)
//        );
//
//        // then
//        resultActions.andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id", is(keycloakUser.getId())))
//                .andExpect(jsonPath("$.email", is(keycloakUser.getEmail())))
//                .andExpect(jsonPath("$.keycloakRoles").isNotEmpty());
//    }
//
//}