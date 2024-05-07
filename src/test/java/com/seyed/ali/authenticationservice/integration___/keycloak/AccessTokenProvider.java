package com.seyed.ali.authenticationservice.integration___.keycloak;

import java.util.Map;

import static io.restassured.RestAssured.given;

public abstract class AccessTokenProvider {

    public String getAccessToken(String username, String password) {
        String CLIENT_ID = "DevVault-v2.0"; // note that, the tests will fail if we change `clientId` field in the realm import json file.
        String CLIENT_SECRET = "some_client_secret"; // we can change it to whatever we want, it's fine.
        String realmUrl = System.getProperty("spring.security.oauth2.resourceserver.jwt.issuer-uri");
        String generateTokenUrl = STR."\{realmUrl}/protocol/openid-connect/token";
        System.out.println("realmUrl: " + realmUrl);

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
                .assertThat().statusCode(200)
                .extract()
                .path("access_token");
        System.out.println("acccessToken: " + accessToken);
        return STR."Bearer \{accessToken}";
    }

}
