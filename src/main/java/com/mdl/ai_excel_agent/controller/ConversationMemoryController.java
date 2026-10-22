package com.mdl.ai_excel_agent.controller;

import com.mdl.ai_excel_agent.dto.ConversationRequest;
import com.mdl.ai_excel_agent.service.ConversationMemoryService;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/ai/interview/conversations")
public class ConversationMemoryController {
    private final ConversationMemoryService conversationMemoryService;

    public ConversationMemoryController(ConversationMemoryService conversationMemoryService) {
        this.conversationMemoryService = conversationMemoryService;
    }

    @PostMapping("/chat")
    public ResponseEntity<Map<String,String>> saveChat(@RequestBody ConversationRequest request)
    {
        if(request.conversationId()==null||request.conversationId().isEmpty()
        ||request.message()==null||request.message().isEmpty())
        {
            return ResponseEntity.badRequest().body(
                    Map.of("error","ConversationID and message are required")
            );
        }
        String answer = conversationMemoryService.chat(
                request.conversationId(),
                request.message()
        );
        return  ResponseEntity.ok(Map.of("message",answer));
    }


    @DeleteMapping("/{conversationId}")
    public ResponseEntity<Map<String,String>> deleteChat(@PathVariable String conversationId)
    {
        if(conversationId==null||conversationId.isEmpty()){
            return ResponseEntity.badRequest().body(Map.of("error","ConversationID is required"));
        }

        conversationMemoryService.clearConversation(conversationId);
        return  ResponseEntity.ok(Map.of("message","Conversation deleted",
                "ConversationId",conversationId));
    }


}
