package com.nashtech.rookies.java05.AssetManagement.dtos.response;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class ErrorResponse {
    String code;
    String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    Map<String, String> validationErrors;

    public ErrorResponse(String code, String message) {
        super();
        this.code = code;
        this.message = message;
    }

    public ErrorResponse(String code, String message, Map<String, String> validationErrors) {
        super();
        this.code = code;
        this.message = message;
        this.validationErrors = validationErrors;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, String> getValidationErrors() {
        return validationErrors;
    }

    public void setValidationErrors(Map<String, String> validationErrors) {
        this.validationErrors = validationErrors;
    }

}