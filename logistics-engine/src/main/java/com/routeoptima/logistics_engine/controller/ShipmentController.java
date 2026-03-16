package com.routeoptima.logistics_engine.controller;

import com.routeoptima.logistics_engine.model.Shipment;
import com.routeoptima.logistics_engine.repository.ShipmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.routeoptima.logistics_engine.service.PricingService;


import java.util.List;

@RestController
@RequestMapping("/api/shipments")
@RequiredArgsConstructor
public class ShipmentController {

    private final ShipmentRepository shipmentRepository;
    private final PricingService pricingService;

    @PostMapping
    public Shipment createShipment(@RequestBody Shipment shipment) {
        Double finalPrice = pricingService.calculatePrice(
            shipment.getOrigin(), 
            shipment.getDestination(), 
            shipment.getBasePrice()
        );
        shipment.setBasePrice(finalPrice);
        return shipmentRepository.save(shipment);
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