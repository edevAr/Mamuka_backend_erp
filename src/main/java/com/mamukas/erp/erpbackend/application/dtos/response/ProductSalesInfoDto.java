package com.mamukas.erp.erpbackend.application.dtos.response;

import java.math.BigDecimal;

/**
 * DTO for product sales information in reports
 */
public class ProductSalesInfoDto {
    
    private Long idProduct;
    private String productName;
    private Integer totalQuantitySold;
    private Integer salesCount;
    private BigDecimal totalRevenue;
    
    // Constructors
    public ProductSalesInfoDto() {}
    
    public ProductSalesInfoDto(Long idProduct, String productName, Integer totalQuantitySold, 
                              Integer salesCount, BigDecimal totalRevenue) {
        this.idProduct = idProduct;
        this.productName = productName;
        this.totalQuantitySold = totalQuantitySold;
        this.salesCount = salesCount;
        this.totalRevenue = totalRevenue;
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
    
    public Integer getTotalQuantitySold() {
        return totalQuantitySold;
    }
    
    public void setTotalQuantitySold(Integer totalQuantitySold) {
        this.totalQuantitySold = totalQuantitySold;
    }
    
    public Integer getSalesCount() {
        return salesCount;
    }
    
    public void setSalesCount(Integer salesCount) {
        this.salesCount = salesCount;
    }
    
    public BigDecimal getTotalRevenue() {
        return totalRevenue;
    }
    
    public void setTotalRevenue(BigDecimal totalRevenue) {
        this.totalRevenue = totalRevenue;
    }
}


