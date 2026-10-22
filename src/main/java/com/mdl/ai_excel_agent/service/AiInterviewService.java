package com.mdl.ai_excel_agent.service;

import com.mdl.ai_excel_agent.dto.PromptRequest;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AiInterviewService {

    private final ChatClient chatClient;

    public AiInterviewService(ChatClient.Builder chatClient) {
        this.chatClient = chatClient.build();
    }

    public String chat(String message) {

        return chatClient
                .prompt()
                .system("""
                        You are a helpful Java backend interview coach.
                        Give clear, technically accurate answers.
                        Use examples when useful.
                        """)
                .user(message)
                .call()
                .content();
    }

    public String answerWithPrompt(PromptRequest request) {

        String templateText = """
                You are a technical interview coach for Java backend developers.

                Topic: {topic}
                Candidate experience level: {experienceLevel}

                Answer the following interview question: {question}

                Follow this structure:
                1. Give a simple definition.
                2. Explain how it works.
                3. Provide a practical Java or Spring Boot example when relevant.
                4. Give a concise interview-ready answer.
                """;


        PromptTemplate promptTemplate =
                new PromptTemplate(templateText);

        Prompt prompt = promptTemplate.create(
                Map.of(
                        "topic", request.topic(),
                        "experienceLevel", request.experincelevel(),
                        "question", request.question()
                )
        );
        return chatClient.
                prompt(prompt)
                .call().
                content();
    }
}
