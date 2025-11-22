package com.mamukas.erp.erpbackend.application.dtos.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDateTime;

/**
 * DTO for sales analysis request
 */
public class SalesAnalysisRequestDto {
    
    // id_store is optional - no validation annotation needed
    // We'll validate manually in the service if needed
    private Long id_store; // Optional - null is allowed
    
    @NotNull(message = "startDate is required")
    private LocalDateTime startDate;
    
    @NotNull(message = "endDate is required")
    private LocalDateTime endDate;
    
    // Constructors
    public SalesAnalysisRequestDto() {}
    
    public SalesAnalysisRequestDto(Long id_store, LocalDateTime startDate, LocalDateTime endDate) {
        this.id_store = id_store;
        this.startDate = startDate;
        this.endDate = endDate;
    }
    
    // Getters and setters
    public Long getId_store() {
        return id_store;
    }
    
    public void setId_store(Long id_store) {
        this.id_store = id_store;
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
}

