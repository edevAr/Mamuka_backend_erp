package com.mamukas.erp.erpbackend.infrastructure.web.controller;

import com.mamukas.erp.erpbackend.application.dtos.request.TransferReportRequestDto;
import com.mamukas.erp.erpbackend.application.dtos.request.WarehouseRequestDto;
import com.mamukas.erp.erpbackend.application.dtos.request.WarehouseTransferRequestDto;
import com.mamukas.erp.erpbackend.application.dtos.request.BoxTransferDto;
import com.mamukas.erp.erpbackend.application.dtos.request.PackageTransferDto;
import com.mamukas.erp.erpbackend.application.dtos.response.MessageResponseDto;
import com.mamukas.erp.erpbackend.application.dtos.response.TransferReportResponseDto;
import com.mamukas.erp.erpbackend.application.dtos.response.WarehouseResponseDto;
import com.mamukas.erp.erpbackend.application.dtos.response.PageResponseDto;
import com.mamukas.erp.erpbackend.application.dtos.response.WarehouseDetailsResponseDto;
import com.mamukas.erp.erpbackend.application.dtos.response.WarehouseBoxDto;
import com.mamukas.erp.erpbackend.application.dtos.response.WarehousePackDto;
import com.mamukas.erp.erpbackend.application.services.WarehouseService;
import com.mamukas.erp.erpbackend.application.services.WarehouseItemService;
import com.mamukas.erp.erpbackend.application.services.BoxService;
import com.mamukas.erp.erpbackend.application.services.PackService;
import com.mamukas.erp.erpbackend.application.services.ProductService;
import com.mamukas.erp.erpbackend.application.services.JwtService;
import com.mamukas.erp.erpbackend.application.services.EmployeeWarehouseService;
import com.mamukas.erp.erpbackend.application.services.UserService;
import com.mamukas.erp.erpbackend.application.dtos.response.WarehouseListItemDto;
import com.mamukas.erp.erpbackend.domain.entities.EmployeeWarehouse;
import com.mamukas.erp.erpbackend.domain.entities.User;
import jakarta.servlet.http.HttpServletRequest;
import com.mamukas.erp.erpbackend.domain.entities.Warehouse;
import com.mamukas.erp.erpbackend.domain.entities.WarehouseItem;
import com.mamukas.erp.erpbackend.domain.entities.Box;
import com.mamukas.erp.erpbackend.domain.entities.Pack;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/warehouses")
@CrossOrigin(origins = "*")
public class WarehouseController {
    
    @Autowired
    private WarehouseService warehouseService;
    
    @Autowired
    private WarehouseItemService warehouseItemService;
    
    @Autowired
    private BoxService boxService;
    
    @Autowired
    private PackService packService;
    
    @Autowired
    private ProductService productService;
    
    @Autowired
    private JwtService jwtService;
    
    @Autowired
    private EmployeeWarehouseService employeeWarehouseService;
    
    @Autowired
    private UserService userService;
    
    @PostMapping
    public ResponseEntity<WarehouseResponseDto> createWarehouse(@Valid @RequestBody WarehouseRequestDto request) {
        try {
            Warehouse warehouse = warehouseService.createWarehouse(request.getName(), request.getAddress());
            if (request.getStatus() != null && !request.getStatus().equals("Active")) {
                warehouse = warehouseService.updateWarehouse(warehouse.getIdWarehouse(), request.getName(), request.getAddress(), request.getStatus());
            }
            
            WarehouseResponseDto response = convertToResponseDto(warehouse);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
    
    @PreAuthorize("hasAuthority('INVENTORY_READ') or hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<PageResponseDto<WarehouseResponseDto>> getAllWarehouses(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "idWarehouse") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDir) {
        try {
            Sort sort = sortDir.equalsIgnoreCase("DESC") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
            Pageable pageable = PageRequest.of(page, size, sort);
            
            Page<Warehouse> warehousePage = warehouseService.findAll(pageable);
            List<WarehouseResponseDto> content = warehousePage.getContent().stream()
                    .map(this::convertToResponseDto)
                    .collect(Collectors.toList());
            
            PageResponseDto<WarehouseResponseDto> response = new PageResponseDto<>(
                content,
                warehousePage.getTotalElements(),
                warehousePage.getTotalPages(),
                warehousePage.getNumber(),
                warehousePage.getSize(),
                warehousePage.hasNext(),
                warehousePage.hasPrevious()
            );
            
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<WarehouseResponseDto> getWarehouseById(@PathVariable Long id) {
        try {
            Optional<Warehouse> warehouse = warehouseService.findById(id);
            if (warehouse.isPresent()) {
                WarehouseResponseDto response = convertToResponseDto(warehouse.get());
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    /**
     * Get detailed warehouse information with boxes and packs
     * Also includes list of other warehouses linked to the authenticated user
     * GET /api/warehouses/{id}/details
     */
    @PreAuthorize("hasAuthority('USER_*') or hasRole('ADMIN')")
    @GetMapping("/{id}/details")
    public ResponseEntity<WarehouseDetailsResponseDto> getWarehouseDetails(@PathVariable Long id, HttpServletRequest request) {
        try {
            Optional<Warehouse> warehouse = warehouseService.findById(id);
            if (!warehouse.isPresent()) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
            
            // Extract idUser from JWT token
            Long idUser = extractUserIdFromToken(request);
            if (idUser == null) {
                return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
            }
            
            WarehouseDetailsResponseDto response = convertToDetailsResponseDto(warehouse.get(), idUser);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("/active")
    public ResponseEntity<List<WarehouseResponseDto>> getActiveWarehouses() {
        try {
            List<Warehouse> warehouses = warehouseService.findActiveWarehouses();
            List<WarehouseResponseDto> response = warehouses.stream()
                    .map(this::convertToResponseDto)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("/status/{status}")
    public ResponseEntity<List<WarehouseResponseDto>> getWarehousesByStatus(@PathVariable String status) {
        try {
            List<Warehouse> warehouses = warehouseService.findByStatus(status);
            List<WarehouseResponseDto> response = warehouses.stream()
                    .map(this::convertToResponseDto)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<WarehouseResponseDto>> searchWarehousesByAddress(@RequestParam String address) {
        try {
            List<Warehouse> warehouses = warehouseService.findByAddressContaining(address);
            List<WarehouseResponseDto> response = warehouses.stream()
                    .map(this::convertToResponseDto)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<WarehouseResponseDto> updateWarehouse(@PathVariable Long id, @Valid @RequestBody WarehouseRequestDto request) {
        try {
            Warehouse warehouse = warehouseService.updateWarehouse(id, request.getName(), request.getAddress(), request.getStatus());
            WarehouseResponseDto response = convertToResponseDto(warehouse);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
    
    @PutMapping("/{id}/activate")
    public ResponseEntity<MessageResponseDto> activateWarehouse(@PathVariable Long id) {
        try {
            warehouseService.activateWarehouse(id);
            return new ResponseEntity<>(new MessageResponseDto("Warehouse activated successfully"), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(new MessageResponseDto("Warehouse not found"), HttpStatus.NOT_FOUND);
        }
    }
    
    @PutMapping("/{id}/deactivate")
    public ResponseEntity<MessageResponseDto> deactivateWarehouse(@PathVariable Long id) {
        try {
            warehouseService.deactivateWarehouse(id);
            return new ResponseEntity<>(new MessageResponseDto("Warehouse deactivated successfully"), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(new MessageResponseDto("Warehouse not found"), HttpStatus.NOT_FOUND);
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponseDto> deleteWarehouse(@PathVariable Long id) {
        try {
            warehouseService.deleteWarehouse(id);
            return new ResponseEntity<>(new MessageResponseDto("Warehouse deleted successfully"), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(new MessageResponseDto("Warehouse not found"), HttpStatus.NOT_FOUND);
        }
    }
    
    /**
     * Transfer products between warehouses
     * POST /api/warehouses/transfer
     */
    @PreAuthorize("hasAuthority('USER_*') or hasRole('ADMIN')")
    @PostMapping("/transfer")
    public ResponseEntity<MessageResponseDto> transferProducts(@Valid @RequestBody WarehouseTransferRequestDto request) {
        try {
            // Validate warehouses exist
            Optional<Warehouse> fromWarehouse = warehouseService.findById(request.getFromWarehouseId());
            if (!fromWarehouse.isPresent()) {
                return new ResponseEntity<>(new MessageResponseDto("Source warehouse not found"), HttpStatus.NOT_FOUND);
            }
            
            Optional<Warehouse> toWarehouse = warehouseService.findById(request.getToWarehouseId());
            if (!toWarehouse.isPresent()) {
                return new ResponseEntity<>(new MessageResponseDto("Destination warehouse not found"), HttpStatus.NOT_FOUND);
            }
            
            if (request.getFromWarehouseId().equals(request.getToWarehouseId())) {
                return new ResponseEntity<>(new MessageResponseDto("Source and destination warehouses cannot be the same"), HttpStatus.BAD_REQUEST);
            }
            
            // Validate that at least one of boxes or packages is provided and not empty
            boolean hasBoxes = request.getBoxes() != null && !request.getBoxes().isEmpty();
            boolean hasPackages = request.getPackages() != null && !request.getPackages().isEmpty();
            
            if (!hasBoxes && !hasPackages) {
                return new ResponseEntity<>(new MessageResponseDto("At least one box or package must be provided for transfer"), HttpStatus.BAD_REQUEST);
            }
            
            // Lists to store newly created box and pack IDs
            List<Long> newBoxIds = new java.util.ArrayList<>();
            List<Long> newPackIds = new java.util.ArrayList<>();
            
            // Process boxes
            if (request.getBoxes() != null && !request.getBoxes().isEmpty()) {
                for (BoxTransferDto boxTransfer : request.getBoxes()) {
                    Long newBoxId = transferBox(boxTransfer);
                    if (newBoxId != null) {
                        newBoxIds.add(newBoxId);
                    }
                }
            }
            
            // Process packages
            if (request.getPackages() != null && !request.getPackages().isEmpty()) {
                for (PackageTransferDto packTransfer : request.getPackages()) {
                    Long newPackId = transferPack(packTransfer);
                    if (newPackId != null) {
                        newPackIds.add(newPackId);
                    }
                }
            }
            
            // Create a single warehouse_item record for new boxes and packs in destination warehouse
            // Status is automatically set to "Transferred" for transfer operations
            Long boxId = newBoxIds.isEmpty() ? null : newBoxIds.get(0);
            Long packId = newPackIds.isEmpty() ? null : newPackIds.get(0);
            String transferStatus = "Transferred";
            
            if (boxId != null || packId != null) {
                try {
                    if (boxId != null && packId != null) {
                        // Both box and pack: create single record with both
                        warehouseItemService.assignBoxAndPackToWarehouse(request.getToWarehouseId(), boxId, packId, transferStatus);
                    } else if (boxId != null) {
                        // Only box: create record with box only
                        warehouseItemService.assignBoxToWarehouse(request.getToWarehouseId(), boxId, transferStatus);
                    } else if (packId != null) {
                        // Only pack: create record with pack only
                        warehouseItemService.assignPackToWarehouse(request.getToWarehouseId(), packId, transferStatus);
                    }
                } catch (Exception e) {
                    // If already assigned, continue
                }
            }
            
            return new ResponseEntity<>(new MessageResponseDto("Transfer completed successfully"), HttpStatus.OK);
            
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new MessageResponseDto(e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(new MessageResponseDto("Error during transfer: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    /**
     * Transfer a box: subtract units from original and create new box
     * @return The ID of the newly created box
     */
    private Long transferBox(BoxTransferDto boxTransfer) {
        // Find the original box
        Optional<Box> originalBoxOpt = boxService.findById(boxTransfer.getId_box());
        if (!originalBoxOpt.isPresent()) {
            throw new IllegalArgumentException("Box with id " + boxTransfer.getId_box() + " not found");
        }
        
        Box originalBox = originalBoxOpt.get();
        
        // Validate sufficient units
        if (originalBox.getUnits() == null || originalBox.getUnits() < boxTransfer.getUnits_boxes()) {
            throw new IllegalArgumentException("Insufficient units in box " + boxTransfer.getId_box() + 
                ". Available: " + (originalBox.getUnits() != null ? originalBox.getUnits() : 0) + 
                ", Requested: " + boxTransfer.getUnits_boxes());
        }
        
        // Subtract units from original box
        Integer newUnits = originalBox.getUnits() - boxTransfer.getUnits_boxes();
        originalBox.setUnits(newUnits);
        
        // Update stock of original box (stock = units * units_box)
        if (originalBox.getUnitsBox() != null && originalBox.getUnitsBox() > 0) {
            originalBox.setStock(newUnits * originalBox.getUnitsBox());
        } else {
            originalBox.setStock(0);
        }
        
        // Save updated original box
        boxService.save(originalBox);
        
        // Create new box with same id_product
        Box newBox = new Box();
        newBox.setIdProduct(originalBox.getIdProduct());
        newBox.setName(originalBox.getName());
        newBox.setExpirationDate(originalBox.getExpirationDate());
        newBox.setUnits(boxTransfer.getUnits_boxes());
        newBox.setUnitsBox(originalBox.getUnitsBox()); // Copy units_box
        // Calculate stock for new box
        if (newBox.getUnitsBox() != null && newBox.getUnitsBox() > 0) {
            newBox.setStock(boxTransfer.getUnits_boxes() * newBox.getUnitsBox());
        } else {
            newBox.setStock(0);
        }
        
        // Save new box and return its ID
        Box savedBox = boxService.save(newBox);
        return savedBox.getIdBox();
    }
    
    /**
     * Transfer a pack: subtract units from original and create new pack
     * @return The ID of the newly created pack
     */
    private Long transferPack(PackageTransferDto packTransfer) {
        // Find the original pack
        Optional<Pack> originalPackOpt = packService.findById(packTransfer.getId_package());
        if (!originalPackOpt.isPresent()) {
            throw new IllegalArgumentException("Pack with id " + packTransfer.getId_package() + " not found");
        }
        
        Pack originalPack = originalPackOpt.get();
        
        // Validate sufficient units
        if (originalPack.getUnits() == null || originalPack.getUnits() < packTransfer.getUnits_packages()) {
            throw new IllegalArgumentException("Insufficient units in pack " + packTransfer.getId_package() + 
                ". Available: " + (originalPack.getUnits() != null ? originalPack.getUnits() : 0) + 
                ", Requested: " + packTransfer.getUnits_packages());
        }
        
        // Subtract units from original pack
        Integer newUnits = originalPack.getUnits() - packTransfer.getUnits_packages();
        originalPack.setUnits(newUnits);
        
        // Update stock of original pack (stock = units * units_pack)
        if (originalPack.getUnitsPack() != null && originalPack.getUnitsPack() > 0) {
            originalPack.setStock(newUnits * originalPack.getUnitsPack());
        } else {
            originalPack.setStock(0);
        }
        
        // Save updated original pack
        packService.save(originalPack);
        
        // Create new pack with same id_product
        Pack newPack = new Pack();
        newPack.setIdProduct(originalPack.getIdProduct());
        newPack.setName(originalPack.getName());
        newPack.setExpirationDate(originalPack.getExpirationDate());
        newPack.setUnits(packTransfer.getUnits_packages());
        newPack.setUnitsPack(originalPack.getUnitsPack()); // Copy units_pack
        // Calculate stock for new pack
        if (newPack.getUnitsPack() != null && newPack.getUnitsPack() > 0) {
            newPack.setStock(packTransfer.getUnits_packages() * newPack.getUnitsPack());
        } else {
            newPack.setStock(0);
        }
        
        // Save new pack and return its ID
        Pack savedPack = packService.save(newPack);
        return savedPack.getIdPack();
    }
    
    private WarehouseResponseDto convertToResponseDto(Warehouse warehouse) {
        Integer totalProducts = calculateTotalProducts(warehouse.getIdWarehouse());
        return new WarehouseResponseDto(
            warehouse.getIdWarehouse(),
            warehouse.getName(),
            warehouse.getAddress(),
            warehouse.getStatus(),
            totalProducts,
            warehouse.getCreatedAt(),
            warehouse.getUpdatedAt()
        );
    }
    
    /**
     * Calculate total number of products in a warehouse
     * This is done by summing the units from all boxes and packs in the warehouse
     */
    private Integer calculateTotalProducts(Long idWarehouse) {
        try {
            // Get all warehouse items for this warehouse
            java.util.List<WarehouseItem> warehouseItems = warehouseItemService.findItemsByWarehouse(idWarehouse);
            
            int totalProducts = 0;
            
            for (WarehouseItem item : warehouseItems) {
                // If warehouse item has a box, add its units
                if (item.getIdBox() != null) {
                    java.util.Optional<Box> box = boxService.findById(item.getIdBox());
                    if (box.isPresent() && box.get().getUnits() != null) {
                        totalProducts += box.get().getUnits();
                    }
                }
                
                // If warehouse item has a pack, add its units
                if (item.getIdPack() != null) {
                    java.util.Optional<Pack> pack = packService.findById(item.getIdPack());
                    if (pack.isPresent() && pack.get().getUnits() != null) {
                        totalProducts += pack.get().getUnits();
                    }
                }
            }
            
            return totalProducts;
        } catch (Exception e) {
            // If there's any error calculating, return 0
            return 0;
        }
    }
    
    /**
     * Convert warehouse to detailed response DTO with boxes and packs
     * Also includes list of other warehouses linked to the user (excluding current warehouse)
     */
    private WarehouseDetailsResponseDto convertToDetailsResponseDto(Warehouse warehouse, Long idUser) {
        // Get all warehouse items for this warehouse
        List<WarehouseItem> warehouseItems = warehouseItemService.findItemsByWarehouse(warehouse.getIdWarehouse());
        
        // Lists for boxes and packs
        List<WarehouseBoxDto> boxes = new java.util.ArrayList<>();
        List<WarehousePackDto> packs = new java.util.ArrayList<>();
        
        for (WarehouseItem item : warehouseItems) {
            // Process boxes
            if (item.getIdBox() != null) {
                Optional<Box> boxOpt = boxService.findById(item.getIdBox());
                if (boxOpt.isPresent()) {
                    Box box = boxOpt.get();
                    String productName = getProductName(box.getIdProduct());
                    
                    WarehouseBoxDto boxDto = new WarehouseBoxDto(
                        productName,
                        box.getIdProduct(),
                        box.getIdBox(),
                        box.getUnits(),
                        box.getUnitsBox(),
                        box.getStock()
                    );
                    boxes.add(boxDto);
                }
            }
            
            // Process packs
            if (item.getIdPack() != null) {
                Optional<Pack> packOpt = packService.findById(item.getIdPack());
                if (packOpt.isPresent()) {
                    Pack pack = packOpt.get();
                    String productName = getProductName(pack.getIdProduct());
                    
                    WarehousePackDto packDto = new WarehousePackDto(
                        productName,
                        pack.getIdProduct(),
                        pack.getIdPack(),
                        pack.getUnits(),
                        pack.getUnitsPack(),
                        pack.getStock()
                    );
                    packs.add(packDto);
                }
            }
        }
        
        // Get warehouses linked to the user (excluding current warehouse)
        List<WarehouseListItemDto> userWarehouses = getUserWarehouses(idUser, warehouse.getIdWarehouse());
        
        // Get warehouse manager name (user associated with this warehouse from employee_warehouses)
        String warehouseManager = getWarehouseManagerName(warehouse.getIdWarehouse());
        
        return new WarehouseDetailsResponseDto(
            warehouse.getIdWarehouse(),
            warehouse.getName(),
            warehouse.getAddress(),
            warehouse.getStatus(),
            warehouseManager,
            userWarehouses,
            boxes,
            packs
        );
    }
    
    /**
     * Get warehouses linked to the user, excluding the current warehouse
     * The warehouseManager for all warehouses in the array is the same: the user from the token
     */
    private List<WarehouseListItemDto> getUserWarehouses(Long idUser, Long excludeWarehouseId) {
        try {
            // Get the warehouse manager name (same for all warehouses: the user from the token)
            String warehouseManager = getUserFullName(idUser);
            
            // Get all employee-warehouse relationships for this user
            List<EmployeeWarehouse> employeeWarehouses = employeeWarehouseService.findWarehousesByEmployee(idUser);
            
            // Convert to WarehouseListItemDto and filter out the current warehouse
            return employeeWarehouses.stream()
                .filter(ew -> !ew.getIdWarehouse().equals(excludeWarehouseId)) // Exclude current warehouse
                .map(ew -> {
                    Optional<Warehouse> warehouseOpt = warehouseService.findById(ew.getIdWarehouse());
                    if (warehouseOpt.isPresent()) {
                        Warehouse w = warehouseOpt.get();
                        // Use the same warehouseManager for all (the user from the token)
                        return new WarehouseListItemDto(w.getIdWarehouse(), w.getName(), warehouseManager);
                    }
                    return null;
                })
                .filter(w -> w != null) // Remove nulls in case warehouse not found
                .collect(Collectors.toList());
        } catch (Exception e) {
            // If there's any error, return empty list
            return new java.util.ArrayList<>();
        }
    }
    
    /**
     * Get the full name (name + lastName) of a user by ID
     */
    private String getUserFullName(Long idUser) {
        try {
            Optional<User> userOpt = userService.findById(idUser);
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                String name = user.getName() != null ? user.getName() : "";
                String lastName = user.getLastName() != null ? user.getLastName() : "";
                return (name + " " + lastName).trim();
            }
            return "";
        } catch (Exception e) {
            return "";
        }
    }
    
    /**
     * Get the warehouse manager name for a given warehouse
     * Returns the name of the first user associated with this warehouse in employee_warehouses
     */
    private String getWarehouseManagerName(Long idWarehouse) {
        try {
            // Get all employees associated with this warehouse
            List<EmployeeWarehouse> employeeWarehouses = employeeWarehouseService.findEmployeesByWarehouse(idWarehouse);
            
            if (!employeeWarehouses.isEmpty()) {
                // Get the first employee's user ID
                Long userId = employeeWarehouses.get(0).getIdUser();
                return getUserFullName(userId);
            }
            return "";
        } catch (Exception e) {
            return "";
        }
    }
    
    /**
     * Extract user ID from JWT token in request
     */
    private Long extractUserIdFromToken(HttpServletRequest request) {
        try {
            String authHeader = request.getHeader("Authorization");
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                return jwtService.extractIdUser(token);
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * Generate transfer report for a warehouse within a date range
     */
    @PreAuthorize("hasAuthority('WIREHOUSES_*') or hasRole('ADMIN')")
    @PostMapping("/transfer-report")
    public ResponseEntity<TransferReportResponseDto> generateTransferReport(@Valid @RequestBody TransferReportRequestDto request) {
        try {
            // Validate date range
            if (request.getStartDate().isAfter(request.getEndDate())) {
                return ResponseEntity.badRequest().build();
            }
            
            TransferReportResponseDto report = warehouseService.generateTransferReport(request);
            return ResponseEntity.ok(report);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Get product name by product ID
     */
    private String getProductName(Long idProduct) {
        try {
            if (idProduct == null) {
                return "Producto desconocido";
            }
            Optional<com.mamukas.erp.erpbackend.domain.entities.Product> product = productService.findById(idProduct);
            if (product.isPresent()) {
                return product.get().getName();
            }
            return "Producto desconocido";
        } catch (Exception e) {
            return "Producto desconocido";
        }
    }
}
