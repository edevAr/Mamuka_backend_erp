package com.mamukas.erp.erpbackend.application.dtos.response;

import java.time.LocalDateTime;

public class WarehouseResponseDto {
    
    private Long idWarehouse;
    private String name;
    private String address;
    private String status;
    private Integer products;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Constructors
    public WarehouseResponseDto() {}
    
    public WarehouseResponseDto(Long idWarehouse, String name, String address, String status, Integer products, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.idWarehouse = idWarehouse;
        this.name = name;
        this.address = address;
        this.status = status;
        this.products = products;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    // Getters and setters
    public Long getIdWarehouse() {
        return idWarehouse;
    }
    
    public void setIdWarehouse(Long idWarehouse) {
        this.idWarehouse = idWarehouse;
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
    
    public Integer getProducts() {
        return products;
    }
    
    public void setProducts(Integer products) {
        this.products = products;
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
}
