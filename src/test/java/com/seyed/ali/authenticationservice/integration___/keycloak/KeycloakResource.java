//package com.seyed.ali.authenticationservice.integration___.keycloak;
//
//import dasniko.testcontainers.keycloak.KeycloakContainer;
//
//import java.util.Map;
//
//public class KeycloakResource implements QuarkusTestResourceLifecycleManager {
//
//    KeycloakContainer keycloakContainer;
//
//    @Override
//    public Map<String, String> start() {
//        this.keycloakContainer = new KeycloakContainer()
//                .withRealmImportFiles("/test-realm/DevVault-v2.0-test-realm.json");
//        this.keycloakContainer.start();
//
//        String realmUrl = STR."\{this.keycloakContainer.getAuthServerUrl()}/realms/DevVault-v2.0";
//        return Map.of(
//                "quarkus.oidc.auth-server-url", realmUrl
//        );
//    }
//
//    @Override
//    public void stop() {
//        if (this.keycloakContainer != null) {
//            this.keycloakContainer.stop();
//        }
//    }
//
//}
