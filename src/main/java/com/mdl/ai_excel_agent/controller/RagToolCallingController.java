package com.mdl.ai_excel_agent.controller;

import com.mdl.ai_excel_agent.dto.RagToolRequest;
import com.mdl.ai_excel_agent.service.RagToolCallingService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
@RequestMapping("/api/ai/interview/rag-tools")
public class RagToolCallingController {

    private final RagToolCallingService ragToolCallingService;

    public RagToolCallingController(RagToolCallingService ragToolCallingService) {
        this.ragToolCallingService = ragToolCallingService;
    }

    @PostMapping("/chat")
    public ResponseEntity<?> chat(@RequestBody RagToolRequest request) {

        if (request.message() == null || request.message().isBlank()) {
            return ResponseEntity.badRequest().body(
                    Map.of("error", "message is required")
            );
        }

        if (request.message().length() > 2000) {
            return ResponseEntity.badRequest().body(
                    Map.of("error", "message must not exceed 2000 characters")
            );
        }

        try {
            String answer = ragToolCallingService.answer(request);

            if (answer == null || answer.isBlank()) {
                return ResponseEntity.status(502).body(
                        Map.of("error", "The AI returned an empty response")
                );
            }

            return ResponseEntity.ok(Map.of("response", answer));

        } catch (Exception e) {
            // Log the exception using your application's normal logger.
            return ResponseEntity.status(502).body(
                    Map.of("error", "RAG tool-calling request failed")
            );
        }
    }
}
