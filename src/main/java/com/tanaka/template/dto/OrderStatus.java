package com.tanaka.template.dto;

import lombok.Data;

public enum OrderStatus {
    PENDING,
    COMPLETED,
    CANCELLED;

    @Data
    public static class ForgotPasswordRequest {
        private String email;
    }
}
