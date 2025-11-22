package com.mamukas.erp.erpbackend.application.dtos.response;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for transfer report response
 */
public class TransferReportResponseDto {
    
    private Long idWarehouse;
    private String warehouseName;
    private String warehouseAddress;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Integer totalTransfers;
    private List<DailyTransfersDto> dailyTransfers;
    
    // Constructors
    public TransferReportResponseDto() {}
    
    public TransferReportResponseDto(Long idWarehouse, String warehouseName, String warehouseAddress,
                                    LocalDateTime startDate, LocalDateTime endDate,
                                    Integer totalTransfers, List<DailyTransfersDto> dailyTransfers) {
        this.idWarehouse = idWarehouse;
        this.warehouseName = warehouseName;
        this.warehouseAddress = warehouseAddress;
        this.startDate = startDate;
        this.endDate = endDate;
        this.totalTransfers = totalTransfers;
        this.dailyTransfers = dailyTransfers;
    }
    
    // Getters and setters
    public Long getIdWarehouse() {
        return idWarehouse;
    }
    
    public void setIdWarehouse(Long idWarehouse) {
        this.idWarehouse = idWarehouse;
    }
    
    public String getWarehouseName() {
        return warehouseName;
    }
    
    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }
    
    public String getWarehouseAddress() {
        return warehouseAddress;
    }
    
    public void setWarehouseAddress(String warehouseAddress) {
        this.warehouseAddress = warehouseAddress;
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
    
    public Integer getTotalTransfers() {
        return totalTransfers;
    }
    
    public void setTotalTransfers(Integer totalTransfers) {
        this.totalTransfers = totalTransfers;
    }
    
    public List<DailyTransfersDto> getDailyTransfers() {
        return dailyTransfers;
    }
    
    public void setDailyTransfers(List<DailyTransfersDto> dailyTransfers) {
        this.dailyTransfers = dailyTransfers;
    }
}


