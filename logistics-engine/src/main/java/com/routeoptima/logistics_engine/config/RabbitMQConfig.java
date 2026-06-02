package com.routeoptima.logistics_engine.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;

@Configuration
public class RabbitMQConfig {

    public static final String QUEUE_NAME = "route_optimization_queue";
    public static final String EXCHANGE_NAME = "route_optimization_exchange";
    public static final String ROUTING_KEY = "route.optimize";

    @Bean
    public Queue queue() {
        return new Queue(QUEUE_NAME, true); // Durable queue survives restarts
    }

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    @Bean
    public Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new JacksonJsonMessageConverter();
    }
}