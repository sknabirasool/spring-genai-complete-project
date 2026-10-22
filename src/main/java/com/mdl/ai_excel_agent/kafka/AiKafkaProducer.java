package com.mdl.ai_excel_agent.kafka;


import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mdl.ai_excel_agent.dto.AiRequestMessage;


@Service
public class AiKafkaProducer {

    private static final String TOPIC = "ai-requests";

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public AiKafkaProducer(
            KafkaTemplate<String, String> kafkaTemplate,
            ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public CompletableFuture<Void> sendRequest(AiRequestMessage request) {
        try {
            String json = objectMapper.writeValueAsString(request);

            return kafkaTemplate.send(
                    TOPIC,
                    request.requestId(),
                    json
            ).thenApply(result -> null);

        } catch (JsonProcessingException e) {
            return CompletableFuture.failedFuture(e);
        }
    }
}
