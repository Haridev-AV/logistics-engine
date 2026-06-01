package com.routeoptima.logistics_engine.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.math.BigDecimal;

@Entity
@Table(name = "shipments")
@Data
public class Shipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String origin;
    private String destination;

    @Enumerated(EnumType.STRING)
    private ShipmentStatus status = ShipmentStatus.PENDING;

    private Double weight;
    @Column(name = "base_price", precision = 10, scale = 2)
    private BigDecimal basePrice;

    private LocalDateTime createdAt = LocalDateTime.now();
}