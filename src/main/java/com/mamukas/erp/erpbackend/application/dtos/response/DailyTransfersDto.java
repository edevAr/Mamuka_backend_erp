package com.mamukas.erp.erpbackend.application.dtos.response;

import java.time.LocalDate;
import java.util.List;

/**
 * DTO for daily transfers information
 */
public class DailyTransfersDto {
    
    private LocalDate date;
    private Integer transfersCount;
    private List<TransferredProductDto> products;
    
    // Constructors
    public DailyTransfersDto() {}
    
    public DailyTransfersDto(LocalDate date, Integer transfersCount, List<TransferredProductDto> products) {
        this.date = date;
        this.transfersCount = transfersCount;
        this.products = products;
    }
    
    // Getters and setters
    public LocalDate getDate() {
        return date;
    }
    
    public void setDate(LocalDate date) {
        this.date = date;
    }
    
    public Integer getTransfersCount() {
        return transfersCount;
    }
    
    public void setTransfersCount(Integer transfersCount) {
        this.transfersCount = transfersCount;
    }
    
    public List<TransferredProductDto> getProducts() {
        return products;
    }
    
    public void setProducts(List<TransferredProductDto> products) {
        this.products = products;
    }
}


