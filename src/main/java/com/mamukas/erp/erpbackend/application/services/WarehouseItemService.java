package com.mamukas.erp.erpbackend.application.services;

import com.mamukas.erp.erpbackend.domain.entities.WarehouseItem;
import com.mamukas.erp.erpbackend.infrastructure.persistence.jpa.WarehouseItemJpaEntity;
import com.mamukas.erp.erpbackend.infrastructure.repositories.WarehouseItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class WarehouseItemService {
    
    @Autowired
    private WarehouseItemRepository warehouseItemRepository;
    
    /**
     * Save warehouse-item relationship
     */
    public WarehouseItem save(WarehouseItem warehouseItem) {
        if (!warehouseItem.isValid()) {
            throw new IllegalArgumentException("WarehouseItem must have at least idBox or idPack");
        }
        
        WarehouseItemJpaEntity entity = warehouseItemRepository.toEntity(warehouseItem);
        WarehouseItemJpaEntity savedEntity = warehouseItemRepository.save(entity);
        return warehouseItemRepository.toDomain(savedEntity);
    }
    
    /**
     * Assign box to warehouse
     */
    public WarehouseItem assignBoxToWarehouse(Long idWarehouse, Long idBox) {
        return assignBoxToWarehouse(idWarehouse, idBox, null);
    }
    
    /**
     * Assign box to warehouse with status
     */
    public WarehouseItem assignBoxToWarehouse(Long idWarehouse, Long idBox, String status) {
        // Check if relationship already exists
        Optional<WarehouseItemJpaEntity> existing = warehouseItemRepository.findByIdWarehouseAndIdBox(idWarehouse, idBox);
        if (existing.isPresent()) {
            throw new RuntimeException("Box is already assigned to this warehouse");
        }
        
        WarehouseItem warehouseItem = new WarehouseItem(idWarehouse, idBox, null, status);
        return save(warehouseItem);
    }
    
    /**
     * Assign pack to warehouse
     */
    public WarehouseItem assignPackToWarehouse(Long idWarehouse, Long idPack) {
        return assignPackToWarehouse(idWarehouse, idPack, null);
    }
    
    /**
     * Assign pack to warehouse with status
     */
    public WarehouseItem assignPackToWarehouse(Long idWarehouse, Long idPack, String status) {
        // Check if relationship already exists
        Optional<WarehouseItemJpaEntity> existing = warehouseItemRepository.findByIdWarehouseAndIdPack(idWarehouse, idPack);
        if (existing.isPresent()) {
            throw new RuntimeException("Pack is already assigned to this warehouse");
        }
        
        WarehouseItem warehouseItem = new WarehouseItem(idWarehouse, null, idPack, status);
        return save(warehouseItem);
    }
    
    /**
     * Assign both box and pack to warehouse
     */
    public WarehouseItem assignBoxAndPackToWarehouse(Long idWarehouse, Long idBox, Long idPack) {
        return assignBoxAndPackToWarehouse(idWarehouse, idBox, idPack, null);
    }
    
    /**
     * Assign both box and pack to warehouse with status
     */
    public WarehouseItem assignBoxAndPackToWarehouse(Long idWarehouse, Long idBox, Long idPack, String status) {
        WarehouseItem warehouseItem = new WarehouseItem(idWarehouse, idBox, idPack, status);
        return save(warehouseItem);
    }
    
    /**
     * Find items by warehouse
     */
    @Transactional(readOnly = true)
    public List<WarehouseItem> findItemsByWarehouse(Long idWarehouse) {
        return warehouseItemRepository.findItemsByWarehouse(idWarehouse)
                .stream()
                .map(warehouseItemRepository::toDomain)
                .collect(Collectors.toList());
    }
    
    /**
     * Find warehouses by box
     */
    @Transactional(readOnly = true)
    public List<WarehouseItem> findWarehousesByBox(Long idBox) {
        return warehouseItemRepository.findWarehousesByBox(idBox)
                .stream()
                .map(warehouseItemRepository::toDomain)
                .collect(Collectors.toList());
    }
    
    /**
     * Find warehouses by pack
     */
    @Transactional(readOnly = true)
    public List<WarehouseItem> findWarehousesByPack(Long idPack) {
        return warehouseItemRepository.findWarehousesByPack(idPack)
                .stream()
                .map(warehouseItemRepository::toDomain)
                .collect(Collectors.toList());
    }
    
    /**
     * Find all warehouse-item relationships
     */
    @Transactional(readOnly = true)
    public List<WarehouseItem> findAll() {
        return warehouseItemRepository.findAll()
                .stream()
                .map(warehouseItemRepository::toDomain)
                .collect(Collectors.toList());
    }
    
    /**
     * Find by ID
     */
    @Transactional(readOnly = true)
    public Optional<WarehouseItem> findById(Long id) {
        Optional<WarehouseItemJpaEntity> entity = warehouseItemRepository.findById(id);
        return entity.map(warehouseItemRepository::toDomain);
    }
    
    /**
     * Find warehouse items by warehouse and date range
     */
    @Transactional(readOnly = true)
    public List<WarehouseItem> findByWarehouseAndDateRange(Long idWarehouse, java.time.LocalDateTime startDate, java.time.LocalDateTime endDate) {
        return warehouseItemRepository.findByWarehouseAndDateRange(idWarehouse, startDate, endDate)
                .stream()
                .map(warehouseItemRepository::toDomain)
                .collect(Collectors.toList());
    }
    
    /**
     * Delete warehouse-item relationship by ID
     */
    public void deleteById(Long id) {
        if (warehouseItemRepository.existsById(id)) {
            warehouseItemRepository.deleteById(id);
        } else {
            throw new RuntimeException("Warehouse-Item relationship not found with id: " + id);
        }
    }
    
    /**
     * Check if box is assigned to warehouse
     */
    @Transactional(readOnly = true)
    public boolean isBoxAssignedToWarehouse(Long idWarehouse, Long idBox) {
        return warehouseItemRepository.findByIdWarehouseAndIdBox(idWarehouse, idBox).isPresent();
    }
    
    /**
     * Check if pack is assigned to warehouse
     */
    @Transactional(readOnly = true)
    public boolean isPackAssignedToWarehouse(Long idWarehouse, Long idPack) {
        return warehouseItemRepository.findByIdWarehouseAndIdPack(idWarehouse, idPack).isPresent();
    }
}

