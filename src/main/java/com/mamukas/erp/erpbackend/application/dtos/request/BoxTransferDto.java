package com.mamukas.erp.erpbackend.application.dtos.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * DTO for box transfer information
 */
public class BoxTransferDto {
    
    @NotNull(message = "id_box is required")
    @Positive(message = "id_box must be positive")
    private Long id_box;
    
    @NotNull(message = "units_boxes is required")
    @Positive(message = "units_boxes must be positive")
    private Integer units_boxes;
    
    // Constructors
    public BoxTransferDto() {}
    
    public BoxTransferDto(Long id_box, Integer units_boxes) {
        this.id_box = id_box;
        this.units_boxes = units_boxes;
    }
    
    // Getters and setters
    public Long getId_box() {
        return id_box;
    }
    
    public void setId_box(Long id_box) {
        this.id_box = id_box;
    }
    
    public Integer getUnits_boxes() {
        return units_boxes;
    }
    
    public void setUnits_boxes(Integer units_boxes) {
        this.units_boxes = units_boxes;
    }
}


