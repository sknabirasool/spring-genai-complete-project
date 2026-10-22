package com.mdl.ai_excel_agent.dto;

public record InterviewGuideRequest(
        String topic,
        String difficulty,
        int questionCount
)
{}
