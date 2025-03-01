package com.bankdata.swiftmanager.exception;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import com.bankdata.swiftmanager.response.ApiResponse;
import com.bankdata.swiftmanager.response.ResponseUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Collections;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ApiResponse<Object>> handleResourceNotFoundException(ResourceNotFoundException ex) {
        ApiResponse<Object> response = ResponseUtil.error(Collections.singletonList(ex.getMessage()), "Resource not found", 404);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }


    @ExceptionHandler({DataIntegrityViolationException.class, ConstraintViolationException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<ApiResponse<Object>> handleDatabaseConstraintViolations(Exception ex) {

        String message = "A database constraint was violated.";
        ApiResponse<Object> response = ResponseUtil.error(Collections.singletonList(ex.getMessage()), "Database Constraint Error", 409);

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(SwiftCodeAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<ApiResponse<Object>> handleSWIFTCodeAlreadyExistsException(SwiftCodeAlreadyExistsException ex) {
        ApiResponse<Object> response = ResponseUtil.error(Collections.singletonList(ex.getMessage()), "Resource already exists.", 409);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }


    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ApiResponse<Object>> handleGeneralException(Exception ex) {
        ApiResponse<Object> response = ResponseUtil.error(Collections.singletonList("An unexpected error occurred"), "Internal Server Error", 500);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

}
