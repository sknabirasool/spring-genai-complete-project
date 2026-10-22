package com.mdl.ai_excel_agent.controller;

import com.mdl.ai_excel_agent.dto.ToolChatRequest;
import com.mdl.ai_excel_agent.service.ToolCallingService;
import com.mdl.ai_excel_agent.tool.InterviewTools;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
@RequestMapping("/api/ai/interview/tools")
public class ToolCallingController {

    private final ToolCallingService toolCallingService;
    public ToolCallingController(ToolCallingService toolCallingService) {
        this.toolCallingService = toolCallingService;
    }


    @PostMapping("/chat")
    public ResponseEntity<?> chat(@RequestBody ToolChatRequest request){
        if(request.message()==null||request.message().isBlank()){
            return ResponseEntity.badRequest().body(
                    Map.of("error","message is required"));
        }
        if(request.message().length() > 2000){
            return ResponseEntity.badRequest().body(Map.of("error","message too long"));
        }

        try{
            String response=toolCallingService.chat(request);
            if (response == null || response.isBlank()) {
                return ResponseEntity.status(502).body(
                        Map.of("error", "The AI returned an empty response")
                );
            }
            return ResponseEntity.ok(Map.of("response", response));
        }catch (Exception e){
            return ResponseEntity.status(502).body(
                    Map.of("error", "AI tool-calling request failed")
            );
        }
    }

}
