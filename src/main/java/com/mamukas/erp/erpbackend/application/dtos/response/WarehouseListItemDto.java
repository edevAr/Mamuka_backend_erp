package com.mamukas.erp.erpbackend.application.dtos.response;

/**
 * DTO for warehouse list item (simplified warehouse info)
 */
public class WarehouseListItemDto {
    
    private Long idWarehouse;
    private String name;
    private String warehouseManager; // Name and last name of the user associated with this warehouse
    
    // Constructors
    public WarehouseListItemDto() {}
    
    public WarehouseListItemDto(Long idWarehouse, String name) {
        this.idWarehouse = idWarehouse;
        this.name = name;
    }
    
    public WarehouseListItemDto(Long idWarehouse, String name, String warehouseManager) {
        this.idWarehouse = idWarehouse;
        this.name = name;
        this.warehouseManager = warehouseManager;
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
    
    public String getWarehouseManager() {
        return warehouseManager;
    }
    
    public void setWarehouseManager(String warehouseManager) {
        this.warehouseManager = warehouseManager;
    }
}

