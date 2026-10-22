package com.mdl.ai_excel_agent.controller;

import com.mdl.ai_excel_agent.dto.EmployeeRequestDTO;
import com.mdl.ai_excel_agent.dto.EmployeeResponseDTO;
import com.mdl.ai_excel_agent.entity.Employee;
import com.mdl.ai_excel_agent.service.EmployeeService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/employee")
public class EmployeeController {

    private static final Logger logger =
            LoggerFactory.getLogger(
                    EmployeeController.class);

    private EmployeeService employeeService;
    @Autowired
    public void setEmployeeService(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping
    public ResponseEntity<EmployeeResponseDTO> createEmployee
            (@Valid @RequestBody EmployeeRequestDTO employeeRequestDTO){
        EmployeeResponseDTO employeeResponseDTO =employeeService.createEmployee(employeeRequestDTO);

        logger.info("Employee created successfully");
        return ResponseEntity.ok(employeeResponseDTO);
    }

    @GetMapping("{id}")
    public ResponseEntity<EmployeeResponseDTO> getEmployeeById(@PathVariable Long id){
        EmployeeResponseDTO employeeResponseDTO=employeeService.getEmployeeById(id);
       logger.info("Employee found successfully");
        return ResponseEntity.ok(employeeResponseDTO);
    }

}
