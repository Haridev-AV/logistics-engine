package com.routeoptima.logistics_engine.controller;

import com.routeoptima.logistics_engine.model.Shipment;
import com.routeoptima.logistics_engine.repository.ShipmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.routeoptima.logistics_engine.service.PricingService;
import com.routeoptima.logistics_engine.dto.DistanceResponse;
import com.routeoptima.logistics_engine.dto.ShipmentResponse;


import java.util.List;
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/shipments")
@RequiredArgsConstructor
public class ShipmentController {

    private final ShipmentRepository shipmentRepository;
    private final PricingService pricingService;

    @PostMapping
    public ResponseEntity<ShipmentResponse> createShipment(
        @RequestBody Shipment shipment,
        @RequestParam(defaultValue = "FASTEST") PricingService.RoutingStrategy strategy) {

        // Now calling calculatePrice with all 4 required arguments
        ShipmentResponse response = pricingService.calculatePrice(
            shipment.getOrigin(),
            shipment.getDestination(),
            shipment.getBasePrice(), // This is the user's input base rate
            strategy
        );

        // Save to DB (optional: map the response back to your Shipment entity first)
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public List<Shipment> getAllShipments() {
        return shipmentRepository.findAll();
    }

    @GetMapping("/{id}")
    public Shipment getShipmentById(@PathVariable Long id) {
        return shipmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Shipment not found"));
    }
}