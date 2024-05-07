package com.seyed.ali.authenticationservice.integration___;

import com.seyed.ali.authenticationservice.config.EurekaClientTestConfiguration;
import dasniko.testcontainers.keycloak.KeycloakContainer;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest({"server.port=0"}) /* random port: `webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT` fails while trying to run the test */
@EnableConfigurationProperties /* to use application.yml-test file */
@ActiveProfiles("test")
@ContextConfiguration(classes = {EurekaClientTestConfiguration.class}) /* to call the configuration in the test (for service-registry configs) */
@Testcontainers
class KeycloakContainerIT {

    //<editor-fold desc="fields">
    @Container
    static KeycloakContainer keycloakContainer = new KeycloakContainer("quay.io/keycloak/keycloak:23.0.7")
            .withRealmImportFiles("/test-realm/DevVault-v2.0-test-realm.json");

    @DynamicPropertySource
    static void registerResourceServerIssuerProperty(DynamicPropertyRegistry registry) {
        keycloakContainer.start();
        registry.add(
                "spring.security.oauth2.resourceserver.jwt.issuer-uri",
                () -> keycloakContainer.getAuthServerUrl() + "/realms/DevVault-v2.0");
    }
    //</editor-fold>

    @Test
    @SuppressWarnings("preview")
    void keycloakRunningTest() {
        // given
        String CLIENT_ID = "DevVault-v2.0"; // note that, the tests will fail if we change `clientId` field in the realm import json file.
        String CLIENT_SECRET = "some_client_secret"; // we can change it to whatever we want, it's fine.
        String authServerUrl = this.keycloakContainer.getAuthServerUrl();
        String adminUsername = this.keycloakContainer.getAdminUsername();
        String adminPassword = this.keycloakContainer.getAdminPassword();

        System.out.println(STR."authServerUrl: \{authServerUrl}");
        System.out.println(STR."adminUsername: \{adminUsername}");
        System.out.println(STR."adminUsername: \{adminPassword}");

        String tokenUrl = STR."\{authServerUrl}/realms/DevVault-v2.0/protocol/openid-connect/token";

        // when
        Response response = given()
                .contentType("application/x-www-form-urlencoded")
                .formParams(Map.of(
                        "username", "default",
                        "password", "1",
                        "grant_type", "password",
                        "client_id", CLIENT_ID,
                        "client_secret", CLIENT_SECRET
                ))
                .post(tokenUrl);

        String accessToken = response
                .then()
                .extract()
                .path("access_token");

        // then
        System.out.println(STR."AccessToken: \{accessToken}");
        assertThat(keycloakContainer.isRunning())
                .isTrue();
        response.then()
                .assertThat()
                .statusCode(200);
    }

    @Test
    void givenAuthenticatedUser_whenGetMe_shouldReturnMyInfo() {
        given().header("Authorization", getAccessToken())
                .when()
                .get("/hello")
                .then()
                .body("username", equalTo("default"))
                .body("lastname", equalTo("Doe"))
                .body("firstname", equalTo("Jane"))
                .body("email", equalTo("jane.doe@baeldung.com"));
    }

    private String getAccessToken() {
        String CLIENT_ID = "DevVault-v2.0"; // note that, the tests will fail if we change `clientId` field in the realm import json file.
        String CLIENT_SECRET = "some_client_secret"; // we can change it to whatever we want, it's fine.
        String generateTokenUrl = STR."\{keycloakContainer.getAuthServerUrl()}/realms/DevVault-v2.0/protocol/openid-connect/token";
        String accessToken = given()
                .contentType("application/x-www-form-urlencoded")
                .formParams(Map.of(
                        "username", "default",
                        "password", "1",
                        "grant_type", "password",
                        "client_id", CLIENT_ID,
                        "client_secret", CLIENT_SECRET
                ))
                .post(generateTokenUrl)
                .then()
                .assertThat().statusCode(200).onFailMessage("Must be success")
                .extract()
                .path("access_token");
        return "Bearer " + accessToken;
    }

}