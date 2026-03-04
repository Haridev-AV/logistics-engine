package com.routeoptima.logistics_engine.repository;

import com.routeoptima.logistics_engine.model.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShipmentRepository extends JpaRepository<Shipment, Long> {
    // Basic CRUD operations are inherited from JpaRepository
}