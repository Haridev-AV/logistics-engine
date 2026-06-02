package com.routeoptima.logistics_engine.queue;

import com.routeoptima.logistics_engine.config.RabbitMQConfig;
import com.routeoptima.logistics_engine.model.Shipment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class QueueProducer {

    private final RabbitTemplate rabbitTemplate;

    public void publishOptimizationTask(Shipment shipment) {
        log.info("Publishing shipment ID {} to the optimization queue...", shipment.getId());
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE_NAME,
                RabbitMQConfig.ROUTING_KEY,
                shipment
        );
    }
}