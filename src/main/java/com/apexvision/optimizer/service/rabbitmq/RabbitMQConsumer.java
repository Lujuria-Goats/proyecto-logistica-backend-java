package com.apexvision.optimizer.service.rabbitmq;

import com.apexvision.optimizer.dtos.RouteRequest;
import com.apexvision.optimizer.dtos.RouteResponse;
import com.apexvision.optimizer.service.RouteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class RabbitMQConsumer {

    private final RouteService routeService;
    private final ObjectMapper objectMapper;


    @RabbitListener(queues = "${myapp.rabbitmq.queue}")
    public void receiveMessage(String message) {
        log.info("Message received by RabbitMQ: {}", message);

        try {
            RouteRequest request = objectMapper.readValue(message, RouteRequest.class);

            log.info("Processing optimization from queue for: {}", request.getFleetId());
            RouteResponse response = routeService.optimizeRoute(request);

            log.info("Optimization completed via RabbitMQ. Distance: {} Km", response.getTotalDistanceKm());

        } catch (Exception e) {
            log.error("Error processing RabbitMQ message: {}", e.getMessage());
        }
    }
}