package com.routeoptima.logistics_engine.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import com.routeoptima.logistics_engine.service.PricingService;

@Data
@AllArgsConstructor
public class ShipmentResponse {
    private double finalPrice;
    private double distanceKm;
    private String formattedETA;
    private PricingService.RoutingStrategy activeStrategy;
}