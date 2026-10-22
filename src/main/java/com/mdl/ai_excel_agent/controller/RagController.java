package com.mdl.ai_excel_agent.controller;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import com.mdl.ai_excel_agent.service.RagService;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/rag")
public class RagController {

    private final RagService ragService;

    public RagController(RagService ragService) {
        this.ragService = ragService;
    }


    @PostMapping(
            value = "/documents",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<?> uploadDocument(
            @RequestParam("file") MultipartFile file) {

        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Please upload a file"));
            }

            String filename = file.getOriginalFilename();

            if (filename == null ||
                    !(filename.toLowerCase().endsWith(".txt")
                            || filename.toLowerCase().endsWith(".pdf"))) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Only .txt and .pdf files are supported"));
            }

            String content;

            if (filename.toLowerCase().endsWith(".pdf")) {
                try (PDDocument document = Loader.loadPDF(file.getBytes())) {
                    content = new PDFTextStripper().getText(document);
                }
            } else {
                content = new String(
                        file.getBytes(),
                        StandardCharsets.UTF_8
                );
            }

            if (content.isBlank()) {
                return ResponseEntity.badRequest()
                        .body(Map.of(
                                "error",
                                "No extractable text found in the document"
                        ));
            }

            int chunks = ragService.indexDocument(filename, content);

            return ResponseEntity.ok(Map.of(
                    "message", "Document indexed successfully",
                    "filename", filename,
                    "chunksCreated", chunks
            ));

        } catch (Exception ex) {
            ex.printStackTrace();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "error", "Document indexing failed",
                            "details", String.valueOf(ex.getMessage())
                    ));
        }
    }


    @PostMapping("/ask")
    public ResponseEntity<?> askQuestion(
            @RequestBody Map<String, String> request) {

        String question = request.get("question");

        if (question == null || question.isBlank()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Question is required"));
        }

        try {
            String answer = ragService.askQuestion(question);

            return ResponseEntity.ok(Map.of(
                    "question", question,
                    "answer", answer
            ));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", ex.getMessage()));
        }
    }
}
