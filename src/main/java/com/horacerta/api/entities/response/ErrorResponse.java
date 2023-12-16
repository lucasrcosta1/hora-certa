package com.horacerta.api.entities.response;

import lombok.Data;

@Data
public class ErrorResponse {
    private String error;
    private String message;

    public ErrorResponse() {}
    public ErrorResponse(String error, String message) {
        this.error = error;
        this.message = message;
    }

}
