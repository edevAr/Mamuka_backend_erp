package com.mamukas.erp.erpbackend.application.dtos.response;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO for daily sales information in reports
 */
public class DailySalesDto {
    
    private LocalDate date;
    private Integer salesCount;
    private BigDecimal totalRevenue;
    
    // Constructors
    public DailySalesDto() {}
    
    public DailySalesDto(LocalDate date, Integer salesCount, BigDecimal totalRevenue) {
        this.date = date;
        this.salesCount = salesCount;
        this.totalRevenue = totalRevenue;
    }
    
    // Getters and setters
    public LocalDate getDate() {
        return date;
    }
    
    public void setDate(LocalDate date) {
        this.date = date;
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


