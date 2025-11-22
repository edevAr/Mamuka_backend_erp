package com.mamukas.erp.erpbackend.infrastructure.web.controller;

import com.mamukas.erp.erpbackend.application.dtos.request.WarehouseItemRequestDto;
import com.mamukas.erp.erpbackend.application.dtos.response.MessageResponseDto;
import com.mamukas.erp.erpbackend.application.dtos.response.WarehouseItemResponseDto;
import com.mamukas.erp.erpbackend.application.services.WarehouseItemService;
import com.mamukas.erp.erpbackend.domain.entities.WarehouseItem;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/warehouse-items")
@CrossOrigin(origins = "*")
@PreAuthorize("hasRole('USER')")
public class WarehouseItemController {
    
    @Autowired
    private WarehouseItemService warehouseItemService;
    
    @PostMapping
    public ResponseEntity<WarehouseItemResponseDto> createWarehouseItem(@Valid @RequestBody WarehouseItemRequestDto request) {
        try {
            WarehouseItem warehouseItem;
            
            // Validate that at least one of idBox or idPack is provided
            if (request.getIdBox() == null && request.getIdPack() == null) {
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }
            
            // Create warehouse item based on what's provided
            if (request.getIdBox() != null && request.getIdPack() != null) {
                warehouseItem = warehouseItemService.assignBoxAndPackToWarehouse(
                    request.getIdWarehouse(), 
                    request.getIdBox(), 
                    request.getIdPack(),
                    request.getStatus()
                );
            } else if (request.getIdBox() != null) {
                warehouseItem = warehouseItemService.assignBoxToWarehouse(
                    request.getIdWarehouse(), 
                    request.getIdBox(),
                    request.getStatus()
                );
            } else {
                warehouseItem = warehouseItemService.assignPackToWarehouse(
                    request.getIdWarehouse(), 
                    request.getIdPack(),
                    request.getStatus()
                );
            }
            
            WarehouseItemResponseDto response = convertToResponseDto(warehouseItem);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
    
    @GetMapping
    public ResponseEntity<List<WarehouseItemResponseDto>> getAllWarehouseItems() {
        try {
            List<WarehouseItem> warehouseItems = warehouseItemService.findAll();
            List<WarehouseItemResponseDto> response = warehouseItems.stream()
                    .map(this::convertToResponseDto)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<WarehouseItemResponseDto> getWarehouseItemById(@PathVariable Long id) {
        try {
            Optional<WarehouseItem> warehouseItem = warehouseItemService.findById(id);
            if (warehouseItem.isPresent()) {
                WarehouseItemResponseDto response = convertToResponseDto(warehouseItem.get());
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("/warehouse/{idWarehouse}")
    public ResponseEntity<List<WarehouseItemResponseDto>> getItemsByWarehouse(@PathVariable Long idWarehouse) {
        try {
            List<WarehouseItem> warehouseItems = warehouseItemService.findItemsByWarehouse(idWarehouse);
            List<WarehouseItemResponseDto> response = warehouseItems.stream()
                    .map(this::convertToResponseDto)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("/box/{idBox}")
    public ResponseEntity<List<WarehouseItemResponseDto>> getWarehousesByBox(@PathVariable Long idBox) {
        try {
            List<WarehouseItem> warehouseItems = warehouseItemService.findWarehousesByBox(idBox);
            List<WarehouseItemResponseDto> response = warehouseItems.stream()
                    .map(this::convertToResponseDto)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("/pack/{idPack}")
    public ResponseEntity<List<WarehouseItemResponseDto>> getWarehousesByPack(@PathVariable Long idPack) {
        try {
            List<WarehouseItem> warehouseItems = warehouseItemService.findWarehousesByPack(idPack);
            List<WarehouseItemResponseDto> response = warehouseItems.stream()
                    .map(this::convertToResponseDto)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("/check/box")
    public ResponseEntity<MessageResponseDto> checkBoxAssignment(@RequestParam Long idWarehouse, @RequestParam Long idBox) {
        try {
            boolean isAssigned = warehouseItemService.isBoxAssignedToWarehouse(idWarehouse, idBox);
            String message = isAssigned ? "Box is assigned to warehouse" : "Box is not assigned to warehouse";
            return new ResponseEntity<>(new MessageResponseDto(message), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(new MessageResponseDto("Error checking assignment"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("/check/pack")
    public ResponseEntity<MessageResponseDto> checkPackAssignment(@RequestParam Long idWarehouse, @RequestParam Long idPack) {
        try {
            boolean isAssigned = warehouseItemService.isPackAssignedToWarehouse(idWarehouse, idPack);
            String message = isAssigned ? "Pack is assigned to warehouse" : "Pack is not assigned to warehouse";
            return new ResponseEntity<>(new MessageResponseDto(message), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(new MessageResponseDto("Error checking assignment"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponseDto> deleteWarehouseItem(@PathVariable Long id) {
        try {
            warehouseItemService.deleteById(id);
            return new ResponseEntity<>(new MessageResponseDto("Warehouse-Item relationship deleted successfully"), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(new MessageResponseDto("Warehouse-Item relationship not found"), HttpStatus.NOT_FOUND);
        }
    }
    
    private WarehouseItemResponseDto convertToResponseDto(WarehouseItem warehouseItem) {
        return new WarehouseItemResponseDto(
            warehouseItem.getIdWarehouseItem(),
            warehouseItem.getIdWarehouse(),
            warehouseItem.getIdBox(),
            warehouseItem.getIdPack(),
            warehouseItem.getStatus(),
            warehouseItem.getCreatedAt(),
            warehouseItem.getUpdatedAt()
        );
    }
}

