package com.bankdata.swiftmanager.exception;

import com.bankdata.swiftmanager.response.ApiResponse;
import com.bankdata.swiftmanager.response.ResponseUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Collections;

@RestControllerAdvice
public class GlobalExceptionHandler {
    // TODO - czy to dobra praktyka?
//    @ExceptionHandler(Exception.class)
//    public ApiResponse<Object> handleGeneralException(Exception ex, HttpServletRequest request) {
//        return ResponseUtil.error(Arrays.asList(ex.getMessage()), "An unexpected error occurred", 1001, request.getRequestURI());
//    }

    @ExceptionHandler(SWIFTCodeNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleResourceNotFoundException(SWIFTCodeNotFoundException ex, HttpServletRequest request) {
        ApiResponse<Object> response = ResponseUtil.error(Collections.singletonList(ex.getMessage()), "Resource not found", 404);
        return ResponseEntity.status(404).body(response);
    }



}
