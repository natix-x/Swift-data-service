package com.bankdata.swiftmanager.response;

import java.util.List;

public class ResponseUtil {
    public static <T> ApiResponse<T> successMessageDisplay(String message) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setMessage(message);
        return response;
    }

    public static <T> ApiResponse<T> error(List<String> errors, String message, int errorCode) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setMessage(message);
        response.setErrors(errors);
        return response;
    }
}
