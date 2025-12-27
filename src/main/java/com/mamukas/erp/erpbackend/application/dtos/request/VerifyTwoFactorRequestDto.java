package com.mamukas.erp.erpbackend.application.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class VerifyTwoFactorRequestDto {
    
    @NotBlank(message = "Username or email is required")
    private String usernameOrEmail;
    
    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;
    
    @NotNull(message = "2FA code is required")
    private Integer twoFactorCode;
    
    private String device;
    private String ip;
    
    // Constructors
    public VerifyTwoFactorRequestDto() {}
    
    public VerifyTwoFactorRequestDto(String usernameOrEmail, String password, Integer twoFactorCode, String device, String ip) {
        this.usernameOrEmail = usernameOrEmail;
        this.password = password;
        this.twoFactorCode = twoFactorCode;
        this.device = device;
        this.ip = ip;
    }
    
    // Getters and setters
    public String getUsernameOrEmail() {
        return usernameOrEmail;
    }
    
    public void setUsernameOrEmail(String usernameOrEmail) {
        this.usernameOrEmail = usernameOrEmail;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public Integer getTwoFactorCode() {
        return twoFactorCode;
    }
    
    public void setTwoFactorCode(Integer twoFactorCode) {
        this.twoFactorCode = twoFactorCode;
    }
    
    public String getDevice() {
        return device;
    }
    
    public void setDevice(String device) {
        this.device = device;
    }
    
    public String getIp() {
        return ip;
    }
    
    public void setIp(String ip) {
        this.ip = ip;
    }
}

