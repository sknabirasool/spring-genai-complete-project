package com.mdl.ai_excel_agent.service;

import com.mdl.ai_excel_agent.dto.EmployeeRequestDTO;
import com.mdl.ai_excel_agent.dto.EmployeeResponseDTO;
import com.mdl.ai_excel_agent.entity.Employee;
import com.mdl.ai_excel_agent.exception.EmailAlreadyExistsException;
import com.mdl.ai_excel_agent.exception.EmployeeNotFoundException;
import com.mdl.ai_excel_agent.repository.EmployeeRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class EmployeeService {

    private EmployeeRepository employeeRepository;

   public EmployeeService(EmployeeRepository employeeRepository) {
       this.employeeRepository = employeeRepository;
   }

    public EmployeeResponseDTO
    createEmployee(@Valid EmployeeRequestDTO employeeRequestDTO) {

        Employee employee = new Employee();

        employee.setName(employeeRequestDTO.getName());
        employee.setEmail(employeeRequestDTO.getEmail());
        employee.setSalary(employeeRequestDTO.getSalary());

        if(employeeRepository.existsByEmail(employee.getEmail())){
            throw new EmailAlreadyExistsException("Email Already Exists");
        }

       Employee saveEmployee=employeeRepository.save(employee);

       EmployeeResponseDTO employeeResponseDTO = new EmployeeResponseDTO();

       employeeResponseDTO.setId(saveEmployee.getId());
       employeeResponseDTO.setName(saveEmployee.getName());
       employeeResponseDTO.setEmail(saveEmployee.getEmail());
       employeeResponseDTO.setSalary(saveEmployee.getSalary());
       return employeeResponseDTO;
    }

    public EmployeeResponseDTO getEmployeeById(Long id) {

       Employee employee=employeeRepository.findById(id)
               .orElseThrow(()-> new EmployeeNotFoundException("Employee Not Found with "+id));

       EmployeeResponseDTO employeeResponseDTO=new EmployeeResponseDTO();

       employeeResponseDTO.setId(employee.getId());
       employeeResponseDTO.setName(employee.getName());
       employeeResponseDTO.setEmail(employee.getEmail());
       employeeResponseDTO.setSalary(employee.getSalary());
       return employeeResponseDTO;
    }
}
