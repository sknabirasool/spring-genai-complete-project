package com.mdl.ai_excel_agent.tool;


import com.mdl.ai_excel_agent.service.RagService;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

@Component
public class RagKnowledgeTool {

    private final RagService ragService;

    public RagKnowledgeTool(RagService ragService) {
        this.ragService = ragService;
    }

    @Tool(description = """
            Search the application's uploaded knowledge documents
            and obtain an answer grounded in those documents.

            Use this tool when the user asks about information that
            may be contained in uploaded banking procedures, policies,
            technical documents, or other RAG knowledge-base content.

            Do not use this tool for unrelated general knowledge questions.
            The input should be the user's specific knowledge question.
            """)
    public String searchKnowledgeBase(
            @ToolParam(
                    description = "Specific question to answer using uploaded knowledge documents"
            )
            String question) {

        if (question == null || question.isBlank()) {
            return "The knowledge search could not run because the question was empty.";
        }

        if (question.length() > 1000) {
            return "The knowledge search question must not exceed 1000 characters.";
        }

        try {
            // Reuse the existing RAG implementation.
            // If your method has a different name, update this line only.
            String answer = ragService.askQuestion(question);

            if (answer == null || answer.isBlank()) {
                return "The knowledge base did not return an answer.";
            }

            return answer;

        } catch (Exception e) {
            // Do not expose internal exception details to the model.
            return "The knowledge search failed. Do not invent an answer from the knowledge base.";
        }
    }
}
