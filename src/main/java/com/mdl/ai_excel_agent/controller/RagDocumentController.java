package com.mdl.ai_excel_agent.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/rag")
public class RagDocumentController {

    private final JdbcTemplate jdbcTemplate;

    public RagDocumentController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }



    @GetMapping("/documents")
    public List<Map<String, Object>> getDocuments() {
        return jdbcTemplate.queryForList("""
            SELECT
                filename,
                COUNT(*) AS chunks,
                MIN(created_at) AS indexed_at
            FROM rag_chunks
            GROUP BY filename
            ORDER BY indexed_at DESC
            """);
    }

    @DeleteMapping("/documents/{filename}")
    public ResponseEntity<?> deleteDocument(
            @PathVariable String filename) {

        int deleted = jdbcTemplate.update(
                "DELETE FROM rag_chunks WHERE filename = ?",
                filename
        );

        if (deleted == 0) {
            return ResponseEntity.status(404)
                    .body(Map.of(
                            "message", "Document not found",
                            "filename", filename
                    ));
        }

        return ResponseEntity.ok(Map.of(
                "message", "Document deleted successfully",
                "filename", filename,
                "chunksDeleted", deleted
        ));
    }
}