package com.mdl.ai_excel_agent.service;

import com.mdl.ai_excel_agent.tool.UserTool;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class AgentService {

    private final ChatClient.Builder chatClientBuilder;
    private final UserTool userTool;

    public AgentService(ChatClient.Builder chatClientBuilder, UserTool userTool) {
        this.chatClientBuilder = chatClientBuilder;
        this.userTool = userTool;
    }

    public String process(String request) {

        ChatClient chatClient = chatClientBuilder
                .defaultTools(userTool)
                .build();

        return chatClient
                .prompt()
                .user(request)
                .call()
                .content();
    }
}
