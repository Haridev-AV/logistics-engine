package com.routeoptima.logistics_engine.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient; // Updated import
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("local")
class AuthControllerIT {

    @Autowired
    private WebTestClient webTestClient; // Replaced TestRestTemplate

    @Test
    void protectedEndpoint_ShouldReturn403Or401_WhenNoTokenProvided() {
        // Act & Assert fluidly using WebTestClient
        webTestClient.get()
                .uri("/api/shipments")
                .exchange()
                .expectStatus().isForbidden(); // Asserts HTTP 403 Forbidden directly
    }
}