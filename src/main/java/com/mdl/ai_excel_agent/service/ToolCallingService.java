package com.mdl.ai_excel_agent.service;

import com.mdl.ai_excel_agent.dto.ToolChatRequest;
import com.mdl.ai_excel_agent.tool.InterviewTools;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class ToolCallingService {

    private final ChatClient chatClient;
    private final InterviewTools interviewTools;

    public ToolCallingService(ChatClient.Builder chatClient, InterviewTools interviewTools) {
        this.chatClient = chatClient.build();
        this.interviewTools = interviewTools;
    }

    public String chat(ToolChatRequest request) {
        return chatClient.prompt()
                .system("""
                        You are a Java backend interview assistant.

                        You can answer general Java and Spring Boot questions.

                        When the user requests a simple-interest calculation,
                        use the calculateSimpleInterest tool.
                        Do not calculate the requested financial result
                        yourself when this tool is available.

                        Explain the tool's result clearly.
                        Do not claim a calculation succeeded if the tool
                        reports that the input is invalid.
                        """)
                .user(request.message())
                .tools(interviewTools)
                .call()
                .content();
    }
}
