package com.mamukas.erp.erpbackend.application.dtos.response;

public class LoginTokenResponseDto {
    private String accessToken;
    private String refreshToken;
    private boolean requiresTwoFactor = false;
    private String message;

    // Constructors
    public LoginTokenResponseDto() {}

    public LoginTokenResponseDto(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.requiresTwoFactor = false;
    }
    
    public LoginTokenResponseDto(boolean requiresTwoFactor, String message) {
        this.requiresTwoFactor = requiresTwoFactor;
        this.message = message;
        this.accessToken = null;
        this.refreshToken = null;
    }

    // Getters and setters
    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
    
    public boolean isRequiresTwoFactor() {
        return requiresTwoFactor;
    }
    
    public void setRequiresTwoFactor(boolean requiresTwoFactor) {
        this.requiresTwoFactor = requiresTwoFactor;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
}
