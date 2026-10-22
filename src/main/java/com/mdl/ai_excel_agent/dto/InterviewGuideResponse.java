package com.mdl.ai_excel_agent.dto;

import java.util.List;

public record InterviewGuideResponse(
        String topic,
        String definition,
        String difficulty,
        List<String> keyPoints,
        List<String> interviewQuestions
)
{
}
