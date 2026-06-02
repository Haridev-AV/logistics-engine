package com.routeoptima.logistics_engine.controller;

import com.routeoptima.logistics_engine.model.Shipment;
import com.routeoptima.logistics_engine.repository.ShipmentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient; // Updated import
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("local")
class ShipmentControllerIT {

    @Autowired
    private WebTestClient webTestClient; // Swapped out TestRestTemplate

    @Autowired
    private ShipmentRepository shipmentRepository;

    @Test
    void getAllShipments_ShouldReturnPaginatedData() {
        // Arrange: Insert mock data if needed, or query the local test instance

        // Act & Assert
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/shipments")
                        .queryParam("page", 0)
                        .queryParam("size", 2)
                        .build())
                .exchange()
                .expectStatus().isOk(); // Asserts HttpStatus.OK directly
    }
}