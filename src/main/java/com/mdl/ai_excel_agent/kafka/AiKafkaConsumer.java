
package com.mdl.ai_excel_agent.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mdl.ai_excel_agent.dto.AiRequestMessage;
import com.mdl.ai_excel_agent.service.AiRequestService;
import com.mdl.ai_excel_agent.service.RagService;
import org.springframework.kafka.annotation.BackOff;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;

import org.springframework.stereotype.Service;


@Service
public class AiKafkaConsumer {

    private final RagService ragService;
    private final AiRequestService requestService;
    private final ObjectMapper objectMapper;

    public AiKafkaConsumer(
            RagService ragService,
            AiRequestService requestService,
            ObjectMapper objectMapper) {
        this.ragService = ragService;
        this.requestService = requestService;
        this.objectMapper = objectMapper;
    }

    @RetryableTopic(
            attempts = "3",
            backOff = @BackOff(
                    delay = 1000,
                    multiplier = 2.0
            ),
            kafkaTemplate = "kafkaTemplate",
            dltTopicSuffix = "-dlt"
    )
    @KafkaListener(
            topics = "ai-requests",
            groupId = "ai-rag-consumer-group"
    )
    public void consume(String json) throws Exception {

        AiRequestMessage request =
                objectMapper.readValue(json, AiRequestMessage.class);

        if (requestService.isCompleted(request.requestId())) {
            System.out.println(
                    "Skipping completed request: " + request.requestId()
            );
            return;
        }

        try {
            requestService.updateStatus(
                    request.requestId(), "PROCESSING"
            );

            if ("TEST_RETRY_FAILURE".equals(request.question())) {
                throw new RuntimeException("Retry mechanism test");
            }

            String answer = ragService.askQuestion(request.question());

            requestService.complete(request.requestId(), answer);

            System.out.println(
                    "AI request completed: " + request.requestId()
            );

        } catch (Exception e) {
            System.err.println(
                    "AI request attempt failed: " + e.getMessage()
            );
            throw e;
        }
    }

    @DltHandler
    public void handleDeadLetter(String json) {
        System.err.println("Message reached dead-letter topic: " + json);

        try {
            AiRequestMessage request =
                    objectMapper.readValue(json, AiRequestMessage.class);

            if (!requestService.isCompleted(request.requestId())) {
                requestService.fail(
                        request.requestId(),
                        "Processing failed after all retry attempts"
                );
            }
        } catch (Exception e) {
            System.err.println(
                    "Could not process dead-letter message: " + e.getMessage()
            );
        }
    }
}
