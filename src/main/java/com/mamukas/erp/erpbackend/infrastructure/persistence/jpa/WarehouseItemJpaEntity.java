package com.mamukas.erp.erpbackend.infrastructure.persistence.jpa;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "warehouse_items")
public class WarehouseItemJpaEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_warehouse_item")
    private Long idWarehouseItem;
    
    @Column(name = "id_warehouse", nullable = false)
    private Long idWarehouse;
    
    @Column(name = "id_box")
    private Long idBox;
    
    @Column(name = "id_pack")
    private Long idPack;
    
    @Column(name = "status", length = 50)
    private String status;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    // Constructors
    public WarehouseItemJpaEntity() {}
    
    public WarehouseItemJpaEntity(Long idWarehouse, Long idBox, Long idPack) {
        this.idWarehouse = idWarehouse;
        this.idBox = idBox;
        this.idPack = idPack;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    public WarehouseItemJpaEntity(Long idWarehouse, Long idBox, Long idPack, String status) {
        this.idWarehouse = idWarehouse;
        this.idBox = idBox;
        this.idPack = idPack;
        this.status = status;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    public WarehouseItemJpaEntity(Long idWarehouseItem, Long idWarehouse, Long idBox, Long idPack, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.idWarehouseItem = idWarehouseItem;
        this.idWarehouse = idWarehouse;
        this.idBox = idBox;
        this.idPack = idPack;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    public WarehouseItemJpaEntity(Long idWarehouseItem, Long idWarehouse, Long idBox, Long idPack, String status, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.idWarehouseItem = idWarehouseItem;
        this.idWarehouse = idWarehouse;
        this.idBox = idBox;
        this.idPack = idPack;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
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

