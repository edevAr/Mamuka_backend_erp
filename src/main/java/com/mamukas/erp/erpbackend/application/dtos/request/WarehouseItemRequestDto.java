package com.mamukas.erp.erpbackend.application.dtos.request;

import jakarta.validation.constraints.NotNull;

public class WarehouseItemRequestDto {
    
    @NotNull(message = "El ID del almacén no puede ser nulo")
    private Long idWarehouse;
    
    private Long idBox;
    
    private Long idPack;
    
    private String status;
    
    // Constructors
    public WarehouseItemRequestDto() {}
    
    public WarehouseItemRequestDto(Long idWarehouse, Long idBox, Long idPack) {
        this.idWarehouse = idWarehouse;
        this.idBox = idBox;
        this.idPack = idPack;
    }
    
    public WarehouseItemRequestDto(Long idWarehouse, Long idBox, Long idPack, String status) {
        this.idWarehouse = idWarehouse;
        this.idBox = idBox;
        this.idPack = idPack;
        this.status = status;
    }
    
    // Getters and setters
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
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
}

