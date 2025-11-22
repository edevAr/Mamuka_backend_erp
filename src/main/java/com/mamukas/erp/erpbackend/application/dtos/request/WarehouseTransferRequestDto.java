package com.mamukas.erp.erpbackend.application.dtos.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.List;

/**
 * DTO for warehouse transfer request
 */
public class WarehouseTransferRequestDto {
    
    @NotNull(message = "fromWarehouseId is required")
    @Positive(message = "fromWarehouseId must be positive")
    private Long fromWarehouseId;
    
    @NotNull(message = "toWarehouseId is required")
    @Positive(message = "toWarehouseId must be positive")
    private Long toWarehouseId;
    
    @Valid
    private List<BoxTransferDto> boxes;
    
    @Valid
    private List<PackageTransferDto> packages;
    
    // Constructors
    public WarehouseTransferRequestDto() {}
    
    public WarehouseTransferRequestDto(Long fromWarehouseId, Long toWarehouseId, 
                                     List<BoxTransferDto> boxes, List<PackageTransferDto> packages) {
        this.fromWarehouseId = fromWarehouseId;
        this.toWarehouseId = toWarehouseId;
        this.boxes = boxes;
        this.packages = packages;
    }
    
    // Getters and setters
    public Long getFromWarehouseId() {
        return fromWarehouseId;
    }
    
    public void setFromWarehouseId(Long fromWarehouseId) {
        this.fromWarehouseId = fromWarehouseId;
    }
    
    public Long getToWarehouseId() {
        return toWarehouseId;
    }
    
    public void setToWarehouseId(Long toWarehouseId) {
        this.toWarehouseId = toWarehouseId;
    }
    
    public List<BoxTransferDto> getBoxes() {
        return boxes;
    }
    
    public void setBoxes(List<BoxTransferDto> boxes) {
        this.boxes = boxes;
    }
    
    public List<PackageTransferDto> getPackages() {
        return packages;
    }
    
    public void setPackages(List<PackageTransferDto> packages) {
        this.packages = packages;
    }
}


