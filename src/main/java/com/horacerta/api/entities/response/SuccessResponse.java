package com.horacerta.api.entities.response;

import lombok.Data;

@Data
public class SuccessResponse {
    private String success;
    private String message;

    public SuccessResponse() {}
    public SuccessResponse(String success, String message) {
        this.success = success;
        this.message = message;
    }

}
