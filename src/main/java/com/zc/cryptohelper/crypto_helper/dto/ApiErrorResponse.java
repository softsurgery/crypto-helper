package com.zc.cryptohelper.crypto_helper.dto;

import lombok.Data;

@Data
public class ApiErrorResponse {
    private String message;
    private String details;
    private int status;

    public ApiErrorResponse(String message, String details, int status) {
        this.message = message;
        this.details = details;
        this.status = status;
    }
}