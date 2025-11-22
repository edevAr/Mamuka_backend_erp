package com.mamukas.erp.erpbackend.application.dtos.response;

import java.util.List;

/**
 * DTO for detailed warehouse information with boxes and packs
 */
public class WarehouseDetailsResponseDto {
    
    private Long idWarehouse;
    private String name;
    private String address;
    private String status;
    private String warehouseManager; // Name and last name of the user from token
    private List<WarehouseListItemDto> warehouses; // List of other warehouses linked to the user
    private List<WarehouseBoxDto> boxes;
    private List<WarehousePackDto> packs;
    
    // Constructors
    public WarehouseDetailsResponseDto() {}
    
    public WarehouseDetailsResponseDto(Long idWarehouse, String name, String address, String status,
                                      String warehouseManager,
                                      List<WarehouseListItemDto> warehouses,
                                      List<WarehouseBoxDto> boxes, List<WarehousePackDto> packs) {
        this.idWarehouse = idWarehouse;
        this.name = name;
        this.address = address;
        this.status = status;
        this.warehouseManager = warehouseManager;
        this.warehouses = warehouses;
        this.boxes = boxes;
        this.packs = packs;
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
    
    public List<WarehouseBoxDto> getBoxes() {
        return boxes;
    }
    
    public void setBoxes(List<WarehouseBoxDto> boxes) {
        this.boxes = boxes;
    }
    
    public List<WarehousePackDto> getPacks() {
        return packs;
    }
    
    public void setPacks(List<WarehousePackDto> packs) {
        this.packs = packs;
    }
    
    public List<WarehouseListItemDto> getWarehouses() {
        return warehouses;
    }
    
    public void setWarehouses(List<WarehouseListItemDto> warehouses) {
        this.warehouses = warehouses;
    }
    
    public String getWarehouseManager() {
        return warehouseManager;
    }
    
    public void setWarehouseManager(String warehouseManager) {
        this.warehouseManager = warehouseManager;
    }
}

