package com.mdl.ai_excel_agent.controller;

import com.mdl.ai_excel_agent.entity.User;
import com.mdl.ai_excel_agent.service.AiService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/ai")
public class AiController {

    private final AiService aiService;
    public AiController(AiService aiService) {
        this.aiService = aiService;
    }

    @PostMapping("/validate")
    public String validateUser(@RequestBody User user) {
        return aiService.validateUser(user);
    }
}
