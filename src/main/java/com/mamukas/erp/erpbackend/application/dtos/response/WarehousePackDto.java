package com.mamukas.erp.erpbackend.application.dtos.response;

/**
 * DTO for pack information in warehouse details
 */
public class WarehousePackDto {
    
    private String product; // Product name
    private Long id_product; // Product ID
    private Long id_pack; // Pack ID
    private Integer amount; // units
    private Integer units_per_pack; // units_pack
    private Integer stock;
    
    // Constructors
    public WarehousePackDto() {}
    
    public WarehousePackDto(String product, Integer amount, Integer units_per_pack, Integer stock) {
        this.product = product;
        this.amount = amount;
        this.units_per_pack = units_per_pack;
        this.stock = stock;
    }
    
    public WarehousePackDto(String product, Long id_product, Integer amount, Integer units_per_pack, Integer stock) {
        this.product = product;
        this.id_product = id_product;
        this.amount = amount;
        this.units_per_pack = units_per_pack;
        this.stock = stock;
    }
    
    public WarehousePackDto(String product, Long id_product, Long id_pack, Integer amount, Integer units_per_pack, Integer stock) {
        this.product = product;
        this.id_product = id_product;
        this.id_pack = id_pack;
        this.amount = amount;
        this.units_per_pack = units_per_pack;
        this.stock = stock;
    }
    
    // Getters and setters
    public String getProduct() {
        return product;
    }
    
    public void setProduct(String product) {
        this.product = product;
    }
    
    public Integer getAmount() {
        return amount;
    }
    
    public void setAmount(Integer amount) {
        this.amount = amount;
    }
    
    public Integer getUnits_per_pack() {
        return units_per_pack;
    }
    
    public void setUnits_per_pack(Integer units_per_pack) {
        this.units_per_pack = units_per_pack;
    }
    
    public Integer getStock() {
        return stock;
    }
    
    public void setStock(Integer stock) {
        this.stock = stock;
    }
    
    public Long getId_product() {
        return id_product;
    }
    
    public void setId_product(Long id_product) {
        this.id_product = id_product;
    }
    
    public Long getId_pack() {
        return id_pack;
    }
    
    public void setId_pack(Long id_pack) {
        this.id_pack = id_pack;
    }
}

