package com.mdl.ai_excel_agent.service;

import com.mdl.ai_excel_agent.dto.RagToolRequest;
import com.mdl.ai_excel_agent.tool.RagKnowledgeTool;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class RagToolCallingService {

    private final ChatClient chatClient;
    private final RagKnowledgeTool  ragKnowledgeTool;

    public RagToolCallingService(ChatClient.Builder chatClient, RagKnowledgeTool ragKnowledgeTool) {
        this.chatClient = chatClient.build();
        this.ragKnowledgeTool = ragKnowledgeTool;
    }

    public String answer(RagToolRequest request) {
        return chatClient.prompt()
                .system("""
                        You are a careful technical and banking-procedure assistant.

                        You have access to a tool named searchKnowledgeBase.
                        The tool searches the application's uploaded knowledge
                        documents using its existing RAG implementation.

                        Rules:
                        1. For questions about uploaded procedures, policies,
                           or project documents, call searchKnowledgeBase.
                        2. Use the returned tool result as the basis for your answer.
                        3. Do not claim that a document states something unless
                           the tool result supports it.
                        4. If the tool reports no answer or a search failure,
                           say that the available knowledge base could not
                           provide the answer.
                        5. For unrelated general questions, answer normally
                           without unnecessarily calling the tool.
                        6. Never invent a retrieved passage or source.
                        """)
                .user(request.message())
                .tools(ragKnowledgeTool)
                .call()
                .content();
    }
}
