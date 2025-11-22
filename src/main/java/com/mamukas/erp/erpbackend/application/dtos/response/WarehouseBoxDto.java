package com.mamukas.erp.erpbackend.application.dtos.response;

/**
 * DTO for box information in warehouse details
 */
public class WarehouseBoxDto {
    
    private String product; // Product name
    private Long id_product; // Product ID
    private Long id_box; // Box ID
    private Integer amount; // units
    private Integer units_per_box; // units_box
    private Integer stock;
    
    // Constructors
    public WarehouseBoxDto() {}
    
    public WarehouseBoxDto(String product, Integer amount, Integer units_per_box, Integer stock) {
        this.product = product;
        this.amount = amount;
        this.units_per_box = units_per_box;
        this.stock = stock;
    }
    
    public WarehouseBoxDto(String product, Long id_product, Integer amount, Integer units_per_box, Integer stock) {
        this.product = product;
        this.id_product = id_product;
        this.amount = amount;
        this.units_per_box = units_per_box;
        this.stock = stock;
    }
    
    public WarehouseBoxDto(String product, Long id_product, Long id_box, Integer amount, Integer units_per_box, Integer stock) {
        this.product = product;
        this.id_product = id_product;
        this.id_box = id_box;
        this.amount = amount;
        this.units_per_box = units_per_box;
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
    
    public Integer getUnits_per_box() {
        return units_per_box;
    }
    
    public void setUnits_per_box(Integer units_per_box) {
        this.units_per_box = units_per_box;
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
    
    public Long getId_box() {
        return id_box;
    }
    
    public void setId_box(Long id_box) {
        this.id_box = id_box;
    }
}

