package com.mdl.ai_excel_agent.controller;

import com.mdl.ai_excel_agent.dto.InterviewGuideRequest;
import com.mdl.ai_excel_agent.dto.InterviewGuideResponse;
import com.mdl.ai_excel_agent.service.StructuredAiService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/ai/interview/structured")
public class StructuredAiController {

    private final StructuredAiService structuredAiService;
    public StructuredAiController(StructuredAiService structuredAiService) {
        this.structuredAiService = structuredAiService;
    }


    @PostMapping("/guide")
    public ResponseEntity<?> guide(@RequestBody InterviewGuideRequest request)
    {
        if(request.topic()==null ||
                request.topic().isBlank()||
                request.difficulty()==null ||
                request.difficulty().isBlank() ||
                request.questionCount() < 1 ||
                request.questionCount() > 10) {

            return ResponseEntity.badRequest().body( Map.of(
                    "error",
                    "Topic and difficulty are required; "
                            + "questionCount must be between 1 and 10"
            ));
        }

        try{
            InterviewGuideResponse response=structuredAiService.generateGuide(request);
            return ResponseEntity.ok().body(response);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "error",
                    "Unable to generate a valid structured AI response"
            ));
        }
    }
}
