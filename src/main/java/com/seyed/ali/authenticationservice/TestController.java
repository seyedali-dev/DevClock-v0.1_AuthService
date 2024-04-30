package com.seyed.ali.authenticationservice;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SecurityRequirement(name = "Keycloak")
public class TestController {

    @GetMapping
    public String hello() {
        return "Hello World";
    }

}
