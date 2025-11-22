package com.mamukas.erp.erpbackend.application.dtos.response;

import java.time.LocalDateTime;

public class WarehouseItemResponseDto {
    
    private Long idWarehouseItem;
    private Long idWarehouse;
    private Long idBox;
    private Long idPack;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Constructors
    public WarehouseItemResponseDto() {}
    
    public WarehouseItemResponseDto(Long idWarehouseItem, Long idWarehouse, Long idBox, Long idPack, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.idWarehouseItem = idWarehouseItem;
        this.idWarehouse = idWarehouse;
        this.idBox = idBox;
        this.idPack = idPack;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    public WarehouseItemResponseDto(Long idWarehouseItem, Long idWarehouse, Long idBox, Long idPack, String status, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.idWarehouseItem = idWarehouseItem;
        this.idWarehouse = idWarehouse;
        this.idBox = idBox;
        this.idPack = idPack;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    // Getters and setters
    public Long getIdWarehouseItem() {
        return idWarehouseItem;
    }
    
    public void setIdWarehouseItem(Long idWarehouseItem) {
        this.idWarehouseItem = idWarehouseItem;
    }
    
    public Long getIdWarehouse() {
        return idWarehouse;
    }
    
    public void setIdWarehouse(Long idWarehouse) {
        this.idWarehouse = idWarehouse;
    }
    
    public Long getIdBox() {
        return idBox;
    }
    
    public void setIdBox(Long idBox) {
        this.idBox = idBox;
    }
    
    public Long getIdPack() {
        return idPack;
    }
    
    public void setIdPack(Long idPack) {
        this.idPack = idPack;
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
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
}

