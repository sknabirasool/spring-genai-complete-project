package com.mdl.ai_excel_agent.service;

import com.mdl.ai_excel_agent.dto.InterviewGuideRequest;
import com.mdl.ai_excel_agent.dto.InterviewGuideResponse;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class StructuredAiService {

    private final ChatClient chatClient;

    public StructuredAiService(ChatClient.Builder chatClient) {
       this.chatClient = chatClient.build();
    }

    public InterviewGuideResponse generateGuide(InterviewGuideRequest request) {

        BeanOutputConverter<InterviewGuideResponse> converter =
                new BeanOutputConverter<>(InterviewGuideResponse.class);

        String templateText = """
                You are a Java backend technical interview coach.

                Generate an interview guide for the following topic.

                Topic: {topic}
                Difficulty: {difficulty}
                Number of interview questions: {questionCount}

                Requirements:
                1. Give a technically accurate, concise definition.
                2. Provide 3 to 5 important key points.
                3. Generate exactly the requested number of interview questions.
                4. Use the requested difficulty level.
                5. Return only JSON matching this required format:
                {format}

                Do not add Markdown code fences or explanatory text
                outside the JSON.
                """;


        PromptTemplate promptTemplate = new PromptTemplate(templateText);

        Prompt prompt=promptTemplate.create(Map.of(
                "topic", request.topic(),
                "difficulty", request.difficulty(),
                "questionCount", request.questionCount(),
                "format", converter.getFormat()
        ));

        String rawResponse=chatClient.prompt(prompt)
                .call()
                .content();

        if(rawResponse==null || rawResponse.isEmpty()){
            throw  new IllegalStateException(
                    "The AI returned an empty response"
            );
        }
        return converter.convert(rawResponse);

    }
}
