package com.example.programacionweb_its_prac1;

public class JsonResponse {
    private String status;
    private String message;
    private Object data;

    public JsonResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }

    public JsonResponse(String status, String message, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    // Getters y Setters
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public Object getData() { return data; }
    public void setData(Object data) { this.data = data; }
}