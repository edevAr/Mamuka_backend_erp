package com.mamukas.erp.erpbackend.application.dtos.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * DTO for package transfer information
 */
public class PackageTransferDto {
    
    @NotNull(message = "id_package is required")
    @Positive(message = "id_package must be positive")
    private Long id_package;
    
    @NotNull(message = "units_packages is required")
    @Positive(message = "units_packages must be positive")
    private Integer units_packages;
    
    // Constructors
    public PackageTransferDto() {}
    
    public PackageTransferDto(Long id_package, Integer units_packages) {
        this.id_package = id_package;
        this.units_packages = units_packages;
    }
    
    // Getters and setters
    public Long getId_package() {
        return id_package;
    }
    
    public void setId_package(Long id_package) {
        this.id_package = id_package;
    }
    
    public Integer getUnits_packages() {
        return units_packages;
    }
    
    public void setUnits_packages(Integer units_packages) {
        this.units_packages = units_packages;
    }
}


