package com.example.web.payments;

public class PaymentResultDTO {
    private boolean success;
    private String message;
    private Long orderId;

    public PaymentResultDTO(boolean success, String message, Long orderId) {
        this.success = success;
        this.message = message;
        this.orderId = orderId;
    }

    // Getters and Setters
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }
}