package com.mdl.ai_excel_agent.controller;

import com.mdl.ai_excel_agent.entity.User;
import com.mdl.ai_excel_agent.service.ExcelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ExcelController {

    private final ExcelService excelService;
    @Autowired
    public ExcelController(ExcelService excelService) {
        this.excelService = excelService;
    }

    @PostMapping("/read")
    public ResponseEntity<List<User>> readFile(@RequestParam("file") MultipartFile file)
            throws IOException {
        return ResponseEntity.ok(excelService.readExcel(file));
    }
}
