package com.mamukas.erp.erpbackend.domain.entities;

import java.time.LocalDateTime;

/**
 * WarehouseItem domain entity representing the relationship between warehouses and items (boxes/packs)
 */
public class WarehouseItem {
    
    private Long idWarehouseItem;
    private Long idWarehouse;
    private Long idBox;
    private Long idPack;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Constructors
    public WarehouseItem() {}
    
    public WarehouseItem(Long idWarehouse, Long idBox, Long idPack) {
        this.idWarehouse = idWarehouse;
        this.idBox = idBox;
        this.idPack = idPack;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    public WarehouseItem(Long idWarehouse, Long idBox, Long idPack, String status) {
        this.idWarehouse = idWarehouse;
        this.idBox = idBox;
        this.idPack = idPack;
        this.status = status;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    public WarehouseItem(Long idWarehouseItem, Long idWarehouse, Long idBox, Long idPack, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.idWarehouseItem = idWarehouseItem;
        this.idWarehouse = idWarehouse;
        this.idBox = idBox;
        this.idPack = idPack;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    public WarehouseItem(Long idWarehouseItem, Long idWarehouse, Long idBox, Long idPack, String status, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.idWarehouseItem = idWarehouseItem;
        this.idWarehouse = idWarehouse;
        this.idBox = idBox;
        this.idPack = idPack;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    // Business methods
    public boolean hasBox() {
        return idBox != null;
    }
    
    public boolean hasPack() {
        return idPack != null;
    }
    
    public boolean hasBoth() {
        return hasBox() && hasPack();
    }
    
    public boolean isValid() {
        // At least one of idBox or idPack must be present
        return idWarehouse != null && (idBox != null || idPack != null);
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
        this.updatedAt = LocalDateTime.now();
    }
    
    public Long getIdBox() {
        return idBox;
    }
    
    public void setIdBox(Long idBox) {
        this.idBox = idBox;
        this.updatedAt = LocalDateTime.now();
    }
    
    public Long getIdPack() {
        return idPack;
    }
    
    public void setIdPack(Long idPack) {
        this.idPack = idPack;
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
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
        this.updatedAt = LocalDateTime.now();
    }
}

