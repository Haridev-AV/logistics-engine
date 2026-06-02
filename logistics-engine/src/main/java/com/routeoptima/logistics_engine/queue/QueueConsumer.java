package com.routeoptima.logistics_engine.queue;

import com.routeoptima.logistics_engine.config.RabbitMQConfig;
import com.routeoptima.logistics_engine.model.Shipment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class QueueConsumer {

    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void processOptimizationTask(Shipment shipment) {
        log.info("📥 [Queue Worker] Received shipment data from queue. Processing ID: {}", shipment.getId());
        
        try {
            // Simulate deep geometric route processing or matrix sorting delays
            Thread.sleep(5000); 
            log.info("[Queue Worker] Route optimization completed successfully for Shipment ID: {}", shipment.getId());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Thread context was interrupted during task processing", e);
        }
    }
}