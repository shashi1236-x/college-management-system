package com.college.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.*;

/**
 * APIResponse - Standardized API response wrapper
 */
public class APIResponse<T> {
    private boolean success;
    private String message;
    private T data;
    private Map<String, Object> metadata;

    public APIResponse() {
        this.metadata = new LinkedHashMap<>();
    }

    public APIResponse(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.metadata = new LinkedHashMap<>();
    }

    // Getters and Setters
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public T getData() { return data; }
    public void setData(T data) { this.data = data; }

    public Map<String, Object> getMetadata() { return metadata; }
    public void setMetadata(Map<String, Object> metadata) { this.metadata = metadata; }

    /**
     * Static helper methods
     */
    public static <T> APIResponse<T> success(String message, T data) {
        return new APIResponse<>(true, message, data);
    }

    public static <T> APIResponse<T> error(String message, T data) {
        return new APIResponse<>(false, message, data);
    }

    public static APIResponse<?> success(String message) {
        return new APIResponse<>(true, message, null);
    }

    public static APIResponse<?> error(String message) {
        return new APIResponse<>(false, message, null);
    }

    public String toJson() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(this);
        } catch (Exception e) {
            return "{\"success\": false, \"message\": \"Error serializing response\"}";
        }
    }

    @Override
    public String toString() {
        return "APIResponse{" +
                "success=" + success +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
