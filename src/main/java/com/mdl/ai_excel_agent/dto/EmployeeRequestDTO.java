package com.mdl.ai_excel_agent.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class EmployeeRequestDTO {

    @NotBlank(message ="Employee name is required")
    private String name;
    @NotBlank(message = "Email is required")
    @Email(message = "Enter valid email")
    private String email;
    @Positive(message = "Salary must be positive Number")
    private double salary;
}
