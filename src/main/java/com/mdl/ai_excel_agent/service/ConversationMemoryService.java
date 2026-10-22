package com.mdl.ai_excel_agent.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.stereotype.Service;

@Service
public class ConversationMemoryService {

    private final ChatClient chatClient;
    private final ChatMemory chatMemory;

    public ConversationMemoryService(
            ChatClient.Builder chatClientBuilder,
            ChatMemory chatMemory) {

        this.chatMemory = chatMemory;

        this.chatClient = chatClientBuilder
                .defaultAdvisors(
                        MessageChatMemoryAdvisor.builder(chatMemory)
                                .build()
                )
                .build();
    }

    public String chat(String conversationId, String message) {

        return chatClient
                .prompt()
                .system("""
                        You are a Java backend interview coach.
                        Use the conversation history to understand
                        follow-up questions.
                        Give clear and technically accurate answers.
                        """)
                .user(message)
                .advisors(advisor -> advisor.param(ChatMemory.CONVERSATION_ID,
                                conversationId))
                .call()
                .content();
    }

    public void clearConversation(String conversationId) {
        chatMemory.clear(conversationId);
    }
}
