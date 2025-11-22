package com.mamukas.erp.erpbackend.application.dtos.response;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for sales analysis response
 */
public class SalesAnalysisResponseDto {
    
    private Long idStore;
    private String storeName;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Integer totalSalesCount;
    private List<SaleResponseDto> sales;
    private ProductSalesInfoDto mostSoldProduct;
    private List<ProductSalesInfoDto> leastSoldProducts;
    
    // Constructors
    public SalesAnalysisResponseDto() {}
    
    public SalesAnalysisResponseDto(Long idStore, String storeName, LocalDateTime startDate, LocalDateTime endDate,
                                   Integer totalSalesCount, List<SaleResponseDto> sales,
                                   ProductSalesInfoDto mostSoldProduct, List<ProductSalesInfoDto> leastSoldProducts) {
        this.idStore = idStore;
        this.storeName = storeName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.totalSalesCount = totalSalesCount;
        this.sales = sales;
        this.mostSoldProduct = mostSoldProduct;
        this.leastSoldProducts = leastSoldProducts;
    }
    
    // Getters and setters
    public Long getIdStore() {
        return idStore;
    }
    
    public void setIdStore(Long idStore) {
        this.idStore = idStore;
    }
    
    public String getStoreName() {
        return storeName;
    }
    
    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }
    
    public LocalDateTime getStartDate() {
        return startDate;
    }
    
    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }
    
    public LocalDateTime getEndDate() {
        return endDate;
    }
    
    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }
    
    public Integer getTotalSalesCount() {
        return totalSalesCount;
    }
    
    public void setTotalSalesCount(Integer totalSalesCount) {
        this.totalSalesCount = totalSalesCount;
    }
    
    public List<SaleResponseDto> getSales() {
        return sales;
    }
    
    public void setSales(List<SaleResponseDto> sales) {
        this.sales = sales;
    }
    
    public ProductSalesInfoDto getMostSoldProduct() {
        return mostSoldProduct;
    }
    
    public void setMostSoldProduct(ProductSalesInfoDto mostSoldProduct) {
        this.mostSoldProduct = mostSoldProduct;
    }
    
    public List<ProductSalesInfoDto> getLeastSoldProducts() {
        return leastSoldProducts;
    }
    
    public void setLeastSoldProducts(List<ProductSalesInfoDto> leastSoldProducts) {
        this.leastSoldProducts = leastSoldProducts;
    }
}


