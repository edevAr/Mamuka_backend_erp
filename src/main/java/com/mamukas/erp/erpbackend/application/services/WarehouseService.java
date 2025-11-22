package com.mamukas.erp.erpbackend.application.services;

import com.mamukas.erp.erpbackend.application.dtos.request.TransferReportRequestDto;
import com.mamukas.erp.erpbackend.application.dtos.response.DailyTransfersDto;
import com.mamukas.erp.erpbackend.application.dtos.response.TransferReportResponseDto;
import com.mamukas.erp.erpbackend.application.dtos.response.TransferredProductDto;
import com.mamukas.erp.erpbackend.domain.entities.Box;
import com.mamukas.erp.erpbackend.domain.entities.Pack;
import com.mamukas.erp.erpbackend.domain.entities.Warehouse;
import com.mamukas.erp.erpbackend.infrastructure.persistence.jpa.WarehouseJpaEntity;
import com.mamukas.erp.erpbackend.infrastructure.repositories.WarehouseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
@Transactional
public class WarehouseService {
    
    @Autowired
    private WarehouseRepository warehouseRepository;
    
    @Autowired
    private WarehouseItemService warehouseItemService;
    
    @Autowired
    private BoxService boxService;
    
    @Autowired
    private PackService packService;
    
    @Autowired
    private ProductService productService;
    
    /**
     * Save warehouse
     */
    public Warehouse save(Warehouse warehouse) {
        WarehouseJpaEntity entity = warehouseRepository.toEntity(warehouse);
        WarehouseJpaEntity savedEntity = warehouseRepository.save(entity);
        return warehouseRepository.toDomain(savedEntity);
    }
    
    /**
     * Create new warehouse with name
     */
    public Warehouse createWarehouse(String name, String address) {
        Warehouse warehouse = new Warehouse(name, address, "Active");
        return save(warehouse);
    }
    
    /**
     * Find warehouse by ID
     */
    @Transactional(readOnly = true)
    public Optional<Warehouse> findById(Long id) {
        Optional<WarehouseJpaEntity> entity = warehouseRepository.findByIdWarehouse(id);
        return entity.map(warehouseRepository::toDomain);
    }
    
    /**
     * Find all warehouses
     */
    @Transactional(readOnly = true)
    public List<Warehouse> findAll() {
        return warehouseRepository.findAll()
                .stream()
                .map(warehouseRepository::toDomain)
                .collect(Collectors.toList());
    }
    
    /**
     * Find all warehouses with pagination
     */
    @Transactional(readOnly = true)
    public Page<Warehouse> findAll(Pageable pageable) {
        return warehouseRepository.findAll(pageable)
                .map(warehouseRepository::toDomain);
    }
    
    /**
     * Find warehouses by status
     */
    @Transactional(readOnly = true)
    public List<Warehouse> findByStatus(String status) {
        return warehouseRepository.findByStatus(status)
                .stream()
                .map(warehouseRepository::toDomain)
                .collect(Collectors.toList());
    }
    
    /**
     * Find active warehouses
     */
    @Transactional(readOnly = true)
    public List<Warehouse> findActiveWarehouses() {
        return findByStatus("Active");
    }
    
    /**
     * Find warehouses by address containing text
     */
    @Transactional(readOnly = true)
    public List<Warehouse> findByAddressContaining(String address) {
        return warehouseRepository.findByAddressContaining(address)
                .stream()
                .map(warehouseRepository::toDomain)
                .collect(Collectors.toList());
    }
    
    /**
     * Update warehouse
     */
    public Warehouse updateWarehouse(Long id, String address, String status) {
        Optional<WarehouseJpaEntity> entityOpt = warehouseRepository.findByIdWarehouse(id);
        if (entityOpt.isPresent()) {
            Warehouse warehouse = warehouseRepository.toDomain(entityOpt.get());
            warehouse.setAddress(address);
            warehouse.setStatus(status);
            return save(warehouse);
        }
        throw new RuntimeException("Warehouse not found with id: " + id);
    }
    
    /**
     * Update warehouse with name
     */
    public Warehouse updateWarehouse(Long id, String name, String address, String status) {
        Optional<WarehouseJpaEntity> entityOpt = warehouseRepository.findByIdWarehouse(id);
        if (entityOpt.isPresent()) {
            Warehouse warehouse = warehouseRepository.toDomain(entityOpt.get());
            warehouse.setName(name);
            warehouse.setAddress(address);
            warehouse.setStatus(status);
            return save(warehouse);
        }
        throw new RuntimeException("Warehouse not found with id: " + id);
    }
    
    /**
     * Activate warehouse
     */
    public Warehouse activateWarehouse(Long id) {
        Optional<WarehouseJpaEntity> entityOpt = warehouseRepository.findByIdWarehouse(id);
        if (entityOpt.isPresent()) {
            Warehouse warehouse = warehouseRepository.toDomain(entityOpt.get());
            warehouse.activate();
            return save(warehouse);
        }
        throw new RuntimeException("Warehouse not found with id: " + id);
    }
    
    /**
     * Deactivate warehouse
     */
    public Warehouse deactivateWarehouse(Long id) {
        Optional<WarehouseJpaEntity> entityOpt = warehouseRepository.findByIdWarehouse(id);
        if (entityOpt.isPresent()) {
            Warehouse warehouse = warehouseRepository.toDomain(entityOpt.get());
            warehouse.deactivate();
            return save(warehouse);
        }
        throw new RuntimeException("Warehouse not found with id: " + id);
    }
    
    /**
     * Delete warehouse
     */
    public void deleteWarehouse(Long id) {
        if (warehouseRepository.existsById(id)) {
            warehouseRepository.deleteById(id);
        } else {
            throw new RuntimeException("Warehouse not found with id: " + id);
        }
    }
    
    /**
     * Generate transfer report for a warehouse within a date range
     */
    @Transactional(readOnly = true)
    public TransferReportResponseDto generateTransferReport(TransferReportRequestDto request) {
        // Validate warehouse exists
        Warehouse warehouse = findById(request.getId_warehouse())
                .orElseThrow(() -> new IllegalArgumentException("Almacén con ID " + request.getId_warehouse() + " no encontrado"));
        
        // Get all warehouse items for this warehouse within date range
        List<com.mamukas.erp.erpbackend.domain.entities.WarehouseItem> warehouseItems = 
                warehouseItemService.findByWarehouseAndDateRange(
                        request.getId_warehouse(),
                        request.getStartDate(),
                        request.getEndDate()
                );
        
        if (warehouseItems.isEmpty()) {
            // Return empty report if no transfers
            return new TransferReportResponseDto(
                    warehouse.getIdWarehouse(),
                    warehouse.getName(),
                    warehouse.getAddress(),
                    request.getStartDate(),
                    request.getEndDate(),
                    0,
                    new ArrayList<>()
            );
        }
        
        // Process each warehouse item to get product information
        List<TransferredProductDto> allProducts = new ArrayList<>();
        
        for (com.mamukas.erp.erpbackend.domain.entities.WarehouseItem item : warehouseItems) {
            // Process box if present
            if (item.getIdBox() != null) {
                Optional<Box> boxOpt = boxService.findById(item.getIdBox());
                if (boxOpt.isPresent()) {
                    Box box = boxOpt.get();
                    String productName = getProductName(box.getIdProduct());
                    allProducts.add(new TransferredProductDto(
                            box.getIdProduct(),
                            productName,
                            box.getIdBox(),
                            null,
                            "box",
                            box.getUnits(),
                            box.getUnitsBox(),
                            null,
                            box.getStock()
                    ));
                }
            }
            
            // Process pack if present
            if (item.getIdPack() != null) {
                Optional<Pack> packOpt = packService.findById(item.getIdPack());
                if (packOpt.isPresent()) {
                    Pack pack = packOpt.get();
                    String productName = getProductName(pack.getIdProduct());
                    allProducts.add(new TransferredProductDto(
                            pack.getIdProduct(),
                            productName,
                            null,
                            pack.getIdPack(),
                            "pack",
                            pack.getUnits(),
                            null,
                            pack.getUnitsPack(),
                            pack.getStock()
                    ));
                }
            }
        }
        
        // Group warehouse items by date (using createdAt)
        Map<LocalDate, List<com.mamukas.erp.erpbackend.domain.entities.WarehouseItem>> itemsByDate = 
                warehouseItems.stream()
                        .filter(item -> item.getCreatedAt() != null)
                        .collect(Collectors.groupingBy(item -> item.getCreatedAt().toLocalDate()));
        
        // Build daily transfers
        List<DailyTransfersDto> dailyTransfers = itemsByDate.entrySet().stream()
                .map(entry -> {
                    LocalDate date = entry.getKey();
                    List<com.mamukas.erp.erpbackend.domain.entities.WarehouseItem> dayItems = entry.getValue();
                    
                    // Get products for this day
                    List<TransferredProductDto> dayProducts = new ArrayList<>();
                    for (com.mamukas.erp.erpbackend.domain.entities.WarehouseItem dayItem : dayItems) {
                        if (dayItem.getIdBox() != null) {
                            Optional<Box> boxOpt = boxService.findById(dayItem.getIdBox());
                            if (boxOpt.isPresent()) {
                                Box box = boxOpt.get();
                                String productName = getProductName(box.getIdProduct());
                                dayProducts.add(new TransferredProductDto(
                                        box.getIdProduct(),
                                        productName,
                                        box.getIdBox(),
                                        null,
                                        "box",
                                        box.getUnits(),
                                        box.getUnitsBox(),
                                        null,
                                        box.getStock()
                                ));
                            }
                        }
                        if (dayItem.getIdPack() != null) {
                            Optional<Pack> packOpt = packService.findById(dayItem.getIdPack());
                            if (packOpt.isPresent()) {
                                Pack pack = packOpt.get();
                                String productName = getProductName(pack.getIdProduct());
                                dayProducts.add(new TransferredProductDto(
                                        pack.getIdProduct(),
                                        productName,
                                        null,
                                        pack.getIdPack(),
                                        "pack",
                                        pack.getUnits(),
                                        null,
                                        pack.getUnitsPack(),
                                        pack.getStock()
                                ));
                            }
                        }
                    }
                    
                    return new DailyTransfersDto(date, dayItems.size(), dayProducts);
                })
                .sorted(Comparator.comparing(DailyTransfersDto::getDate))
                .collect(Collectors.toList());
        
        // Build and return report
        return new TransferReportResponseDto(
                warehouse.getIdWarehouse(),
                warehouse.getName(),
                warehouse.getAddress(),
                request.getStartDate(),
                request.getEndDate(),
                warehouseItems.size(),
                dailyTransfers
        );
    }
    
    /**
     * Get product name by ID
     */
    private String getProductName(Long idProduct) {
        if (idProduct == null) {
            return "Producto desconocido";
        }
        return productService.findById(idProduct)
                .map(com.mamukas.erp.erpbackend.domain.entities.Product::getName)
                .orElse("Producto desconocido");
    }
}
