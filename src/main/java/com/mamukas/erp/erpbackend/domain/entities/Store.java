package com.mamukas.erp.erpbackend.domain.entities;

import java.time.LocalDateTime;

/**
 * Store domain entity representing store information
 */
public class Store {
    
    private Long idStore;
    private String name;
    private String address;
    private String status; // Open, Close
    private String businessHours;
    private Long idCompany;
    private Double rate; // Rating between 1.0 and 5.0
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Constructors
    public Store() {}
    
    public Store(String name, String address, String status, String businessHours, Long idCompany) {
        this.name = name;
        this.address = address;
        this.status = status;
        this.businessHours = businessHours;
        this.idCompany = idCompany;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    public Store(String name, String address, String status, String businessHours) {
        this(name, address, status, businessHours, null);
    }
    
    public Store(Long idStore, String name, String address, String status, String businessHours, 
                Long idCompany, Double rate, LocalDateTime createdAt, LocalDateTime updatedAt) {
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
    
    // Business methods
    public void open() {
        this.status = "Open";
        this.updatedAt = LocalDateTime.now();
    }
    
    public void close() {
        this.status = "Close";
        this.updatedAt = LocalDateTime.now();
    }
    
    public boolean isOpen() {
        return "Open".equals(this.status);
    }
    
    public boolean isClosed() {
        return "Close".equals(this.status);
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
        this.updatedAt = LocalDateTime.now();
    }
    
    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
        this.updatedAt = LocalDateTime.now();
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
        this.updatedAt = LocalDateTime.now();
    }
    
    public String getBusinessHours() {
        return businessHours;
    }
    
    public void setBusinessHours(String businessHours) {
        this.businessHours = businessHours;
        this.updatedAt = LocalDateTime.now();
    }
    
    public Long getIdCompany() {
        return idCompany;
    }
    
    public void setIdCompany(Long idCompany) {
        this.idCompany = idCompany;
        this.updatedAt = LocalDateTime.now();
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
    
    public Double getRate() {
        return rate;
    }
    
    public void setRate(Double rate) {
        this.rate = rate;
        this.updatedAt = LocalDateTime.now();
    }
}
