package com.mdl.ai_excel_agent.entity;


import lombok.Data;

public record AiRequestMessage(
        String requestId,
        String question
)
{
}