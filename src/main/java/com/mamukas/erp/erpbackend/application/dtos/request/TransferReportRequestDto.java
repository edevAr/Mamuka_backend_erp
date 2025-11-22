package com.mamukas.erp.erpbackend.application.dtos.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDateTime;

/**
 * DTO for transfer report request
 */
public class TransferReportRequestDto {
    
    @NotNull(message = "id_warehouse is required")
    @Positive(message = "id_warehouse must be positive")
    private Long id_warehouse;
    
    @NotNull(message = "startDate is required")
    private LocalDateTime startDate;
    
    @NotNull(message = "endDate is required")
    private LocalDateTime endDate;
    
    // Constructors
    public TransferReportRequestDto() {}
    
    public TransferReportRequestDto(Long id_warehouse, LocalDateTime startDate, LocalDateTime endDate) {
        this.id_warehouse = id_warehouse;
        this.startDate = startDate;
        this.endDate = endDate;
    }
    
    // Getters and setters
    public Long getId_warehouse() {
        return id_warehouse;
    }
    
    public void setId_warehouse(Long id_warehouse) {
        this.id_warehouse = id_warehouse;
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


