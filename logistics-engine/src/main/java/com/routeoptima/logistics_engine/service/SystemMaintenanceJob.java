package com.routeoptima.logistics_engine.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SystemMaintenanceJob {

    // Runs every day at midnight to archive stale data
    // Format: Second, Minute, Hour, Day-of-Month, Month, Day-of-Week
    @Scheduled(cron = "0 0 0 * * *")
    public void archiveOldShipments() {
        log.info("Starting nightly background data cleanup job...");
        // This is where you would place query logic like:
        // shipmentRepository.archiveExpiredOrders();
        log.info("Nightly background data cleanup completed successfully.");
    }

    // For testing locally right now: runs every 30 seconds
    @Scheduled(fixedRate = 30000)
    public void logSystemMetrics() {
        log.info("[System Monitor] Logging route engine core health metrics...");
    }
}