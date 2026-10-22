package com.mdl.ai_excel_agent.controller;

import com.mdl.ai_excel_agent.dto.ChatRequest;
import com.mdl.ai_excel_agent.dto.PromptRequest;
import com.mdl.ai_excel_agent.service.AiInterviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
@RequestMapping("/api/ai/interview")
public class AiInterviewController {
    private final AiInterviewService aiInterviewService;

    public AiInterviewController(AiInterviewService aiInterviewService) {
        this.aiInterviewService = aiInterviewService;
    }

    @PostMapping("/chat")
    public ResponseEntity<Map<String, String>> chat(@RequestBody ChatRequest request){
        if(request.message()==null || request.message().isBlank()){
            return ResponseEntity.badRequest().body(
                    Map.of("error","Message must not be empty!")
            );
        }
        String answer = aiInterviewService.chat(request.message());
        return ResponseEntity.ok(Map.of("answer",answer));
    }

    @PostMapping("/prompt")
    public ResponseEntity<Map<String, String>> prompt(@RequestBody PromptRequest request){
        if(request.topic()==null || request.topic().isBlank()||
                request.experincelevel()==null||request.experincelevel().isBlank()||
        request.question()==null||request.question().isBlank()){
            return ResponseEntity.badRequest().
                    body(Map.of("error","Topic, experienceLevel and question are required"));
        }

        String answer=aiInterviewService.answerWithPrompt(request);

        return ResponseEntity.ok(Map.of("answer",answer));

    }






}
