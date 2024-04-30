package com.seyed.ali.authenticationservice.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHeaders;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureWebTestClient
public class TestControllerIT {

    //<editor-fold desc="fields">
    private @Autowired WebTestClient webTestClient;
    private @Autowired WebClient webClient;
    private @Autowired ObjectMapper objectMapper;
    private @Value("${springdoc.swagger-ui.oauth.client-id}") String clientId;
    private @Value("${springdoc.swagger-ui.oauth.client-secret}") String clientSecret;
    private String token;
    //</editor-fold>

    @BeforeEach
    void setUp() {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "password");
        formData.add("client_id", this.clientId);
        formData.add("client_secret", this.clientSecret);
        formData.add("username", "default");
        formData.add("password", "1");

        String jwtToken = this.webClient
                .post()
                .uri("http://localhost:8080/realms/DevVault-v2.0/protocol/openid-connect/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(formData))
                .retrieve()
                .bodyToMono(String.class)
                .block();
        try {
            Map accessToken = this.objectMapper.readValue(jwtToken, Map.class);
            this.token = STR."Bearer \{accessToken.get("access_token").toString()}";
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void helloTest() {
        WebTestClient.ResponseSpec exchange = this.webTestClient
                .get()
                .uri("")
                .header(HttpHeaders.AUTHORIZATION, this.token)
                .exchange();

        exchange.expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo("Hello World");
    }

}
