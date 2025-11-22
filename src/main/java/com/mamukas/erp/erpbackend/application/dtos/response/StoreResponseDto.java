package com.mamukas.erp.erpbackend.application.dtos.response;

import java.time.LocalDateTime;
import java.util.List;

public class StoreResponseDto {
    
    private Long idStore;
    private String name;
    private String address;
    private String status;
    private String businessHours;
    private Long idCompany;
    private Double rate; // Rating between 1.0 and 5.0
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String username; // Name from users table of employee linked to this store
    
    // Constructors
    public StoreResponseDto() {}
    
    public StoreResponseDto(Long idStore, String name, String address, String status, String businessHours, Long idCompany, Double rate, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.idStore = idStore;
        this.name = name;
        this.address = address;
        this.status = status;
        this.businessHours = businessHours;
        this.idCompany = idCompany;
        this.rate = rate;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    public StoreResponseDto(Long idStore, String name, String address, String status, String businessHours, Long idCompany, Double rate, LocalDateTime createdAt, LocalDateTime updatedAt, String username) {
        this.idStore = idStore;
        this.name = name;
        this.address = address;
        this.status = status;
        this.businessHours = businessHours;
        this.idCompany = idCompany;
        this.rate = rate;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.username = username;
    }
    
    // Getters and setters
    public Long getIdStore() {
        return idStore;
    }
    
    public void setIdStore(Long idStore) {
        this.idStore = idStore;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getBusinessHours() {
        return businessHours;
    }
    
    public void setBusinessHours(String businessHours) {
        this.businessHours = businessHours;
    }
    
    public Long getIdCompany() {
        return idCompany;
    }
    
    public void setIdCompany(Long idCompany) {
        this.idCompany = idCompany;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public Double getRate() {
        return rate;
    }
    
    public void setRate(Double rate) {
        this.rate = rate;
    }
}
