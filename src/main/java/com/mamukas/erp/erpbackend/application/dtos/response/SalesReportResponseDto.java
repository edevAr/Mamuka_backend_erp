package com.mamukas.erp.erpbackend.application.dtos.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for sales report response
 */
public class SalesReportResponseDto {
    
    private Long idStore;
    private String storeName;
    private String storeAddress;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Integer totalSalesCount;
    private BigDecimal totalSubtotal;
    private BigDecimal totalDiscount;
    private BigDecimal totalNet;
    private BigDecimal averageSale;
    private ProductSalesInfoDto mostSoldProduct;
    private List<ProductSalesInfoDto> leastSoldProducts;
    private List<DailySalesDto> dailySales;
    private List<SaleResponseDto> sales;
    
    // Constructors
    public SalesReportResponseDto() {}
    
    public SalesReportResponseDto(Long idStore, String storeName, String storeAddress,
                                 LocalDateTime startDate, LocalDateTime endDate,
                                 Integer totalSalesCount, BigDecimal totalSubtotal,
                                 BigDecimal totalDiscount, BigDecimal totalNet,
                                 BigDecimal averageSale, ProductSalesInfoDto mostSoldProduct,
                                 List<ProductSalesInfoDto> leastSoldProducts, 
                                 List<DailySalesDto> dailySales, List<SaleResponseDto> sales) {
        this.idStore = idStore;
        this.storeName = storeName;
        this.storeAddress = storeAddress;
        this.startDate = startDate;
        this.endDate = endDate;
        this.totalSalesCount = totalSalesCount;
        this.totalSubtotal = totalSubtotal;
        this.totalDiscount = totalDiscount;
        this.totalNet = totalNet;
        this.averageSale = averageSale;
        this.mostSoldProduct = mostSoldProduct;
        this.leastSoldProducts = leastSoldProducts;
        this.dailySales = dailySales;
        this.sales = sales;
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
    
    public String getStoreAddress() {
        return storeAddress;
    }
    
    public void setStoreAddress(String storeAddress) {
        this.storeAddress = storeAddress;
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
    
    public BigDecimal getTotalSubtotal() {
        return totalSubtotal;
    }
    
    public void setTotalSubtotal(BigDecimal totalSubtotal) {
        this.totalSubtotal = totalSubtotal;
    }
    
    public BigDecimal getTotalDiscount() {
        return totalDiscount;
    }
    
    public void setTotalDiscount(BigDecimal totalDiscount) {
        this.totalDiscount = totalDiscount;
    }
    
    public BigDecimal getTotalNet() {
        return totalNet;
    }
    
    public void setTotalNet(BigDecimal totalNet) {
        this.totalNet = totalNet;
    }
    
    public BigDecimal getAverageSale() {
        return averageSale;
    }
    
    public void setAverageSale(BigDecimal averageSale) {
        this.averageSale = averageSale;
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
    
    public List<DailySalesDto> getDailySales() {
        return dailySales;
    }
    
    public void setDailySales(List<DailySalesDto> dailySales) {
        this.dailySales = dailySales;
    }
}

