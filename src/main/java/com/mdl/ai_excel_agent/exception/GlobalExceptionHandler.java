package com.mdl.ai_excel_agent.exception;


import com.mdl.ai_excel_agent.dto.ApiErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;


@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log =
            LoggerFactory.getLogger(GlobalExceptionHandler.class);


    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiErrorResponse> handleBadRequest(
            IllegalArgumentException ex,
            HttpServletRequest request) {

        ApiErrorResponse response = new ApiErrorResponse(
                Instant.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                ex.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleUnexpectedError(
            Exception ex,
            HttpServletRequest request) {

        log.error("Unexpected API error at {}", request.getRequestURI(), ex);

        ApiErrorResponse response = new ApiErrorResponse(
                Instant.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                "An unexpected error occurred. Please try again later.",
                request.getRequestURI()
        );

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response);
    }

        @ExceptionHandler(
                MethodArgumentNotValidException.class)
        public ResponseEntity<Map<String,String>> handleValidation(
                MethodArgumentNotValidException ex){

            Map<String,String> errors =
                    new HashMap<>();

            ex.getBindingResult()
                    .getFieldErrors()
                    .forEach(error ->
                            errors.put(
                                    error.getField(),
                                    error.getDefaultMessage()
                            ));

            return ResponseEntity
                    .badRequest()
                    .body(errors);
        }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ErrorResponseDTO> handleEmailAlreadyExists(
            EmailAlreadyExistsException ex, HttpServletRequest request) {

        ErrorResponseDTO errorResponse = new ErrorResponseDTO(
                LocalDateTime.now(),
                HttpStatus.CONFLICT.value(),
                "Conflict",
                ex.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(errorResponse);
    }

    @ExceptionHandler(EmployeeNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> EmployeeNotFoundException(EmployeeNotFoundException ex, HttpServletRequest request) {
            ErrorResponseDTO errorResponse = new ErrorResponseDTO();

            errorResponse.setMessage(ex.getMessage());
            errorResponse.setStatus(HttpStatus.NOT_FOUND.value());
            errorResponse.setTimestamp(LocalDateTime.now());
            errorResponse.setPath(request.getRequestURI());
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(errorResponse);

    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponseDTO> handleRuntimeException(RuntimeException ex, HttpServletRequest request) {
            ErrorResponseDTO errorResponse = new ErrorResponseDTO();
            errorResponse.setMessage(ex.getMessage());
            errorResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            errorResponse.setTimestamp(LocalDateTime.now());
            errorResponse.setPath(request.getRequestURI());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse);

    }


}
