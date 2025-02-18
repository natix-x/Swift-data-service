package com.bankdata.swiftmanager.response;

import java.util.Collections;
import java.util.List;

public class ResponseUtil {
    public static <T> ApiResponse<T> successMessageDisplay(String message) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setMessage(message);
        return response;
    }

    public static <T> ApiResponse<T> successDataDisplay(T data) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setData(data);
        return response;
    }

    public static <T> ApiResponse<T> error(List<String> errors, String message, int errorCode) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setMessage(message);
        response.setErrors(errors);
        response.setErrorCode(errorCode);
        return response;
    }

    public static <T> ApiResponse<T> error(String error, String message, int errorCode) {
        return error(Collections.singletonList(error), message, errorCode);
    }
}
