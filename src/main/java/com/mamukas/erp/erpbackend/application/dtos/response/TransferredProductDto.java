package com.mamukas.erp.erpbackend.application.dtos.response;

/**
 * DTO for transferred product information
 */
public class TransferredProductDto {
    
    private Long idProduct;
    private String productName;
    private Long idBox;
    private Long idPack;
    private String type; // "box" or "pack"
    private Integer units;
    private Integer unitsPerBox;
    private Integer unitsPerPack;
    private Integer stock;
    
    // Constructors
    public TransferredProductDto() {}
    
    public TransferredProductDto(Long idProduct, String productName, Long idBox, Long idPack,
                                String type, Integer units, Integer unitsPerBox, 
                                Integer unitsPerPack, Integer stock) {
        this.idProduct = idProduct;
        this.productName = productName;
        this.idBox = idBox;
        this.idPack = idPack;
        this.type = type;
        this.units = units;
        this.unitsPerBox = unitsPerBox;
        this.unitsPerPack = unitsPerPack;
        this.stock = stock;
    }
    
    // Getters and setters
    public Long getIdProduct() {
        return idProduct;
    }
    
    public void setIdProduct(Long idProduct) {
        this.idProduct = idProduct;
    }
    
    public String getProductName() {
        return productName;
    }
    
    public void setProductName(String productName) {
        this.productName = productName;
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
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public Integer getUnits() {
        return units;
    }
    
    public void setUnits(Integer units) {
        this.units = units;
    }
    
    public Integer getUnitsPerBox() {
        return unitsPerBox;
    }
    
    public void setUnitsPerBox(Integer unitsPerBox) {
        this.unitsPerBox = unitsPerBox;
    }
    
    public Integer getUnitsPerPack() {
        return unitsPerPack;
    }
    
    public void setUnitsPerPack(Integer unitsPerPack) {
        this.unitsPerPack = unitsPerPack;
    }
    
    public Integer getStock() {
        return stock;
    }
    
    public void setStock(Integer stock) {
        this.stock = stock;
    }
}


