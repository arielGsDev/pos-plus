package com.soft.pos_plus.api.shared.response;

import java.util.List;

public class ApiResponse<T> {

    private final String status;
    private final String message;
    private final T data;
    private final List<String> errors;

    public ApiResponse(String status, String message, T data, List<String> errors) {
        this.status = status;
        this.message = message;
        this.data = data;
        this.errors = errors;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }

    public List<String> getErrors() {
        return errors;
    }

    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>("success", message, data, List.of());
    }

    public static ApiResponse<Object> error(String message, List<String> errors) {
        return new ApiResponse<>("error", message, null, errors);
    }
}
