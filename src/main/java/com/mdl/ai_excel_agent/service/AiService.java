package com.mdl.ai_excel_agent.service;


import com.mdl.ai_excel_agent.entity.User;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class AiService {

    private final ChatClient chatClient;

    public AiService(ChatClient.Builder builder) {
       this.chatClient = builder.build();
    }

    public String validateUser(User user) {

        String prompt = """
                Validate the following user data.

                Name: %s
                Email: %s
                Age: %d
                Department: %s

                Check:
                1. Name should not be empty.
                2. Email should look valid.
                3. Age should be between 18 and 100.
                4. Department should not be empty.

                Return:
                VALID or INVALID

                Also give a short reason.
                """.formatted(
                user.getName(),
                user.getEmail(),
                user.getAge(),
                user.getDepartment()
        );

        return chatClient
                .prompt()
                .user(prompt)
                .call()
                .content();
    }
}
