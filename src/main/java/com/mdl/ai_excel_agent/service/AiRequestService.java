package com.mdl.ai_excel_agent.service;


import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AiRequestService {

    private final JdbcTemplate jdbcTemplate;

    public AiRequestService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void create(String requestId, String question) {
        jdbcTemplate.update(
                """
                INSERT INTO ai_requests
                    (request_id, question, status)
                VALUES (?, ?, 'PENDING')
                """,
                requestId, question
        );
    }

    public void updateStatus(String requestId, String status) {
        jdbcTemplate.update(
                """
                UPDATE ai_requests
                SET status = ?
                WHERE request_id = ?
                """,
                status, requestId
        );
    }

    public void complete(String requestId, String answer) {
        jdbcTemplate.update(
                """
                UPDATE ai_requests
                SET answer = ?, status = 'COMPLETED'
                WHERE request_id = ?
                """,
                answer, requestId
        );
    }

    public void fail(String requestId, String error) {
        jdbcTemplate.update(
                """
                UPDATE ai_requests
                SET status = 'FAILED', error_message = ?
                WHERE request_id = ?
                """,
                error, requestId
        );
    }
    public Map<String, Object> findByRequestId(String requestId) {
        return jdbcTemplate.queryForMap("""
                SELECT request_id, question, answer, status,
                       error_message, created_at, updated_at
                FROM ai_requests
                WHERE request_id = ?
                """, requestId);
    }

    public boolean isCompleted(String requestId) {
        Integer count = jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM ai_requests
                WHERE request_id = ?
                  AND status = 'COMPLETED'
                """,
                Integer.class,
                requestId
        );

        return count != null && count > 0;
    }
}