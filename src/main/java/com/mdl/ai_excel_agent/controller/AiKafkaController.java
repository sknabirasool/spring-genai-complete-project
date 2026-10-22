package com.mdl.ai_excel_agent.controller;


import com.mdl.ai_excel_agent.kafka.AiKafkaProducer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import com.mdl.ai_excel_agent.dto.AiRequestMessage;
import com.mdl.ai_excel_agent.service.AiRequestService;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;


@RestController
@RequestMapping("/api/ai-kafka")
public class AiKafkaController {

    private final AiKafkaProducer producer;
    private final AiRequestService requestService;

    public AiKafkaController(
            AiKafkaProducer producer,
            AiRequestService requestService) {
        this.producer = producer;
        this.requestService = requestService;
    }

    @PostMapping("/publish")
    public CompletableFuture<ResponseEntity<Map<String, String>>> publish(
            @RequestBody Map<String, String> body) {

        String question = body.get("message");

        if (question == null || question.isBlank()) {
            return CompletableFuture.completedFuture(
                    ResponseEntity.badRequest().body(
                            Map.of("error", "message is required")
                    )
            );
        }

        String requestId = UUID.randomUUID().toString();

        try {
            requestService.create(requestId, question);
        } catch (Exception e) {
            return CompletableFuture.completedFuture(
                    ResponseEntity.internalServerError().body(
                            Map.of("error", "Could not save AI request")
                    )
            );
        }

        AiRequestMessage request =
                new AiRequestMessage(requestId, question);

        return producer.sendRequest(request)
                .thenApply(result -> ResponseEntity.accepted().body(
                        Map.of(
                                "requestId", requestId,
                                "status", "PENDING",
                                "message", "AI request submitted"
                        )
                ))
                .exceptionally(ex -> {
                    try {
                        requestService.fail(
                                requestId,
                                "Kafka publish failed"
                        );
                    } catch (Exception ignored) {
                        // The original publish failure remains the primary error.
                    }

                    return ResponseEntity.internalServerError().body(
                            Map.of(
                                    "requestId", requestId,
                                    "status", "FAILED",
                                    "error", "Could not publish request to Kafka"
                            )
                    );
                });
    }

    @GetMapping("/requests/{requestId}")
    public ResponseEntity<?> getRequest(@PathVariable String requestId) {
        try {
            return ResponseEntity.ok(
                    requestService.findByRequestId(requestId)
            );
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.status(404).body(
                    Map.of("error", "Request not found")
            );
        }
    }
}
