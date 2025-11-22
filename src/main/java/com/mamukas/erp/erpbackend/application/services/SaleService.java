package com.mamukas.erp.erpbackend.application.services;

import com.mamukas.erp.erpbackend.application.dtos.request.SaleRequestDto;
import com.mamukas.erp.erpbackend.application.dtos.request.SalesAnalysisRequestDto;
import com.mamukas.erp.erpbackend.application.dtos.request.SalesReportRequestDto;
import com.mamukas.erp.erpbackend.application.dtos.response.DailySalesDto;
import com.mamukas.erp.erpbackend.application.dtos.response.ProductSalesInfoDto;
import com.mamukas.erp.erpbackend.application.dtos.response.SaleResponseDto;
import com.mamukas.erp.erpbackend.application.dtos.response.SalesAnalysisResponseDto;
import com.mamukas.erp.erpbackend.application.dtos.response.SalesReportResponseDto;
import com.mamukas.erp.erpbackend.domain.entities.Sale;
import com.mamukas.erp.erpbackend.domain.entities.Box;
import com.mamukas.erp.erpbackend.domain.entities.Pack;
import com.mamukas.erp.erpbackend.infrastructure.persistence.jpa.SaleJpaEntity;
import com.mamukas.erp.erpbackend.infrastructure.repositories.SaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class SaleService {

    private final SaleRepository saleRepository;

    @Autowired
    private BoxService boxService;

    @Autowired
    private PackService packService;
    
    @Autowired
    private EmployeeStoreService employeeStoreService;
    
    @Autowired
    private StoreService storeService;
    
    @Autowired
    private ProductService productService;
    
    @Autowired
    private CustomerService customerService;

    public SaleService(SaleRepository saleRepository) {
        this.saleRepository = saleRepository;
    }

    /**
     * Create a new sale
     */
    public SaleResponseDto createSale(SaleRequestDto request) {
        // Create domain entity
        Sale sale;
        if (request.getIdEmployeeStore() != null) {
            sale = new Sale(
                    request.getIdProduct(),
                    request.getIdCustomer(),
                    request.getIdEmployeeStore(),
                    request.getAmount(),
                    request.getSubtotal(),
                    request.getDiscount()
            );
        } else {
            sale = new Sale(
                    request.getIdProduct(),
                    request.getIdCustomer(),
                    request.getAmount(),
                    request.getSubtotal(),
                    request.getDiscount()
            );
        }
        
        // Set custom date if provided
        if (request.getDate() != null) {
            sale.setDate(request.getDate());
        }
        
        // Save sale
        Sale savedSale = save(sale);
        
        // Update stock in boxes and packs
        updateStockAfterSale(savedSale.getIdProduct(), savedSale.getAmount());
        
        return mapToResponseDto(savedSale);
    }

    /**
     * Get all sales
     */
    @Transactional(readOnly = true)
    public List<SaleResponseDto> getAllSales() {
        return saleRepository.findAll()
                .stream()
                .map(saleRepository::toDomain)
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * Get sale by ID
     */
    @Transactional(readOnly = true)
    public SaleResponseDto getSaleById(Long id) {
        return saleRepository.findById(id)
                .map(saleRepository::toDomain)
                .map(this::mapToResponseDto)
                .orElseThrow(() -> new IllegalArgumentException("Venta con ID " + id + " no encontrada"));
    }

    /**
     * Update a sale
     */
    public SaleResponseDto updateSale(Long id, SaleRequestDto request) {
        Sale existingSale = saleRepository.findById(id)
                .map(saleRepository::toDomain)
                .orElseThrow(() -> new IllegalArgumentException("Venta con ID " + id + " no encontrada"));
        
        // Update fields
        if (request.getDate() != null) {
            existingSale.setDate(request.getDate());
        }
        existingSale.setIdProduct(request.getIdProduct());
        existingSale.setIdCustomer(request.getIdCustomer());
        existingSale.setIdEmployeeStore(request.getIdEmployeeStore());
        existingSale.setAmount(request.getAmount());
        existingSale.setSubtotal(request.getSubtotal());
        existingSale.setDiscount(request.getDiscount());
        
        Sale savedSale = save(existingSale);
        
        return mapToResponseDto(savedSale);
    }

    /**
     * Delete a sale
     */
    public boolean deleteSale(Long id) {
        if (!saleRepository.existsById(id)) {
            throw new IllegalArgumentException("Venta con ID " + id + " no encontrada");
        }
        
        saleRepository.deleteById(id);
        return true;
    }

    /**
     * Get sales by product
     */
    @Transactional(readOnly = true)
    public List<SaleResponseDto> getSalesByProduct(Long idProduct) {
        return saleRepository.findByIdProduct(idProduct)
                .stream()
                .map(saleRepository::toDomain)
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * Get sales by customer
     */
    @Transactional(readOnly = true)
    public List<SaleResponseDto> getSalesByCustomer(Long idCustomer) {
        return saleRepository.findByIdCustomer(idCustomer)
                .stream()
                .map(saleRepository::toDomain)
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * Get sales by employee store
     */
    @Transactional(readOnly = true)
    public List<SaleResponseDto> getSalesByEmployeeStore(Long idEmployeeStore) {
        return saleRepository.findByIdEmployeeStore(idEmployeeStore)
                .stream()
                .map(saleRepository::toDomain)
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * Get sales by date range
     */
    @Transactional(readOnly = true)
    public List<SaleResponseDto> getSalesByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return saleRepository.findByDateBetween(startDate, endDate)
                .stream()
                .map(saleRepository::toDomain)
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * Get sales by total range
     */
    @Transactional(readOnly = true)
    public List<SaleResponseDto> getSalesByTotalRange(BigDecimal minTotal, BigDecimal maxTotal) {
        return saleRepository.findByTotalBetween(minTotal, maxTotal)
                .stream()
                .map(saleRepository::toDomain)
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * Get sales by customer and date range
     */
    @Transactional(readOnly = true)
    public List<SaleResponseDto> getSalesByCustomerAndDateRange(Long idCustomer, LocalDateTime startDate, LocalDateTime endDate) {
        return saleRepository.findByCustomerAndDateRange(idCustomer, startDate, endDate)
                .stream()
                .map(saleRepository::toDomain)
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * Get sales by product and date range
     */
    @Transactional(readOnly = true)
    public List<SaleResponseDto> getSalesByProductAndDateRange(Long idProduct, LocalDateTime startDate, LocalDateTime endDate) {
        return saleRepository.findByProductAndDateRange(idProduct, startDate, endDate)
                .stream()
                .map(saleRepository::toDomain)
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * Get total sales by date range
     */
    @Transactional(readOnly = true)
    public BigDecimal getTotalSalesByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        BigDecimal total = saleRepository.getTotalSalesByDateRange(startDate, endDate);
        return total != null ? total : BigDecimal.ZERO;
    }

    /**
     * Get total sales by customer
     */
    @Transactional(readOnly = true)
    public BigDecimal getTotalSalesByCustomer(Long idCustomer) {
        BigDecimal total = saleRepository.getTotalSalesByCustomer(idCustomer);
        return total != null ? total : BigDecimal.ZERO;
    }

    /**
     * Apply discount to sale
     */
    public SaleResponseDto applyDiscount(Long id, BigDecimal discount) {
        Sale sale = saleRepository.findById(id)
                .map(saleRepository::toDomain)
                .orElseThrow(() -> new IllegalArgumentException("Venta con ID " + id + " no encontrada"));
        
        sale.applyDiscount(discount);
        Sale savedSale = save(sale);
        
        return mapToResponseDto(savedSale);
    }

    /**
     * Check if sale exists by ID
     */
    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        return saleRepository.existsById(id);
    }

    /**
     * Get sale count
     */
    @Transactional(readOnly = true)
    public long getSaleCount() {
        return saleRepository.count();
    }

    /**
     * Find sale by ID (optional)
     */
    @Transactional(readOnly = true)
    public java.util.Optional<Sale> findById(Long id) {
        return saleRepository.findById(id).map(saleRepository::toDomain);
    }

    /**
     * Save sale (for direct domain operations)
     */
    public Sale save(Sale sale) {
        SaleJpaEntity jpaEntity = saleRepository.toJpaEntity(sale);
        SaleJpaEntity saved = saleRepository.save(jpaEntity);
        return saleRepository.toDomain(saved);
    }

    /**
     * Update stock in boxes and packs after a sale
     * 
     * Logic:
     * - If amount > units_box (or units_pack):
     *   - Quotient = amount / units_box (or units_pack)
     *   - Remainder = amount % units_box (or units_pack)
     *   - If quotient > 1: subtract quotient from units
     *   - If remainder > 0: subtract remainder from stock
     * - If amount <= units_box (or units_pack):
     *   - Subtract amount from stock only
     */
    private void updateStockAfterSale(Long idProduct, Integer amount) {
        if (idProduct == null || amount == null || amount <= 0) {
            return;
        }

        // Get all boxes and packs for this product
        List<Box> boxes = boxService.findByIdProduct(idProduct);
        List<Pack> packs = packService.findByIdProduct(idProduct);

        // Update boxes
        for (Box box : boxes) {
            if (box.getUnitsBox() != null && box.getUnitsBox() > 0) {
                updateBoxStock(box, amount);
            }
        }

        // Update packs
        for (Pack pack : packs) {
            if (pack.getUnitsPack() != null && pack.getUnitsPack() > 0) {
                updatePackStock(pack, amount);
            }
        }
    }

    /**
     * Update stock for a box based on sale amount
     * 
     * Logic:
     * - If amount > units_box:
     *   - quotient = amount / units_box
     *   - remainder = amount % units_box
     *   - If quotient > 1: subtract quotient from units
     *   - If remainder > 0: subtract remainder from stock
     * - If amount <= units_box: only subtract amount from stock
     */
    private void updateBoxStock(Box box, Integer amount) {
        Integer unitsBox = box.getUnitsBox();
        
        if (unitsBox == null || unitsBox <= 0) {
            return; // Cannot process if unitsBox is invalid
        }
        
        if (amount > unitsBox) {
            // Divide amount by units_box
            int quotient = amount / unitsBox;
            int remainder = amount % unitsBox;
            
            // If quotient > 1, subtract quotient from units
            if (quotient > 1) {
                Integer currentUnits = box.getUnits() != null ? box.getUnits() : 0;
                if (currentUnits >= quotient) {
                    box.setUnits(currentUnits - quotient);
                } else {
                    // Not enough units, subtract what we can
                    box.setUnits(0);
                }
            }
            
            // If remainder > 0, subtract remainder from stock
            if (remainder > 0) {
                Integer currentStock = box.getStock() != null ? box.getStock() : 0;
                if (currentStock >= remainder) {
                    box.setStock(currentStock - remainder);
                } else {
                    // Not enough stock, set to 0
                    box.setStock(0);
                }
            }
        } else {
            // amount <= units_box: only subtract from stock
            Integer currentStock = box.getStock() != null ? box.getStock() : 0;
            if (currentStock >= amount) {
                box.setStock(currentStock - amount);
            } else {
                // Not enough stock, set to 0
                box.setStock(0);
            }
        }
        
        // Save updated box
        boxService.save(box);
    }

    /**
     * Update stock for a pack based on sale amount
     * 
     * Logic:
     * - If amount > units_pack:
     *   - quotient = amount / units_pack
     *   - remainder = amount % units_pack
     *   - If quotient > 1: subtract quotient from units
     *   - If remainder > 0: subtract remainder from stock
     * - If amount <= units_pack: only subtract amount from stock
     */
    private void updatePackStock(Pack pack, Integer amount) {
        Integer unitsPack = pack.getUnitsPack();
        
        if (unitsPack == null || unitsPack <= 0) {
            return; // Cannot process if unitsPack is invalid
        }
        
        if (amount > unitsPack) {
            // Divide amount by units_pack
            int quotient = amount / unitsPack;
            int remainder = amount % unitsPack;
            
            // If quotient > 1, subtract quotient from units
            if (quotient > 1) {
                Integer currentUnits = pack.getUnits() != null ? pack.getUnits() : 0;
                if (currentUnits >= quotient) {
                    pack.setUnits(currentUnits - quotient);
                } else {
                    // Not enough units, subtract what we can
                    pack.setUnits(0);
                }
            }
            
            // If remainder > 0, subtract remainder from stock
            if (remainder > 0) {
                Integer currentStock = pack.getStock() != null ? pack.getStock() : 0;
                if (currentStock >= remainder) {
                    pack.setStock(currentStock - remainder);
                } else {
                    // Not enough stock, set to 0
                    pack.setStock(0);
                }
            }
        } else {
            // amount <= units_pack: only subtract from stock
            Integer currentStock = pack.getStock() != null ? pack.getStock() : 0;
            if (currentStock >= amount) {
                pack.setStock(currentStock - amount);
            } else {
                // Not enough stock, set to 0
                pack.setStock(0);
            }
        }
        
        // Save updated pack
        packService.save(pack);
    }

    /**
     * Generate sales report for a store within a date range
     */
    @Transactional(readOnly = true)
    public SalesReportResponseDto generateSalesReport(SalesReportRequestDto request) {
        // Validate store exists
        com.mamukas.erp.erpbackend.domain.entities.Store store = storeService.findById(request.getId_store())
                .orElseThrow(() -> new IllegalArgumentException("Tienda con ID " + request.getId_store() + " no encontrada"));
        
        // Get all employee-store assignments for this store
        List<com.mamukas.erp.erpbackend.application.dtos.response.EmployeeStoreResponseDto> employeeStores = 
                employeeStoreService.getEmployeesByStore(request.getId_store());
        
        if (employeeStores.isEmpty()) {
            // Return empty report if no employees assigned to store
            return new SalesReportResponseDto(
                    store.getIdStore(),
                    store.getName(),
                    store.getAddress(),
                    request.getStartDate(),
                    request.getEndDate(),
                    0,
                    BigDecimal.ZERO,
                    BigDecimal.ZERO,
                    BigDecimal.ZERO,
                    BigDecimal.ZERO,
                    null,
                    new ArrayList<>(),
                    new ArrayList<>(),
                    new ArrayList<>()
            );
        }
        
        // Get all id_employee_store values
        List<Long> employeeStoreIds = employeeStores.stream()
                .map(com.mamukas.erp.erpbackend.application.dtos.response.EmployeeStoreResponseDto::getIdEmployeeStore)
                .collect(Collectors.toList());
        
        // Get all sales for these employee stores within date range
        List<Sale> allSales = new ArrayList<>();
        for (Long employeeStoreId : employeeStoreIds) {
            List<SaleJpaEntity> salesEntities = saleRepository.findByEmployeeStoreAndDateRange(
                    employeeStoreId,
                    request.getStartDate(),
                    request.getEndDate()
            );
            allSales.addAll(salesEntities.stream()
                    .map(saleRepository::toDomain)
                    .collect(Collectors.toList()));
        }
        
        // Convert sales to response DTOs with product and customer names
        List<SaleResponseDto> salesDtos = allSales.stream()
                .map(sale -> {
                    String productName = getProductName(sale.getIdProduct());
                    String customerName = getCustomerName(sale.getIdCustomer());
                    return new SaleResponseDto(
                            sale.getIdSale(),
                            sale.getDate(),
                            sale.getIdProduct(),
                            sale.getIdCustomer(),
                            sale.getIdEmployeeStore(),
                            productName,
                            customerName,
                            sale.getAmount(),
                            sale.getSubtotal(),
                            sale.getDiscount(),
                            sale.getTotal(),
                            sale.getCreatedAt(),
                            sale.getUpdatedAt()
                    );
                })
                .collect(Collectors.toList());
        
        // Calculate totals
        BigDecimal totalSubtotal = allSales.stream()
                .map(sale -> sale.getSubtotal() != null ? sale.getSubtotal() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        BigDecimal totalDiscount = allSales.stream()
                .map(sale -> sale.getDiscount() != null ? sale.getDiscount() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        BigDecimal totalNet = allSales.stream()
                .map(sale -> sale.getTotal() != null ? sale.getTotal() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        // Calculate average sale
        BigDecimal averageSale = BigDecimal.ZERO;
        if (!allSales.isEmpty()) {
            averageSale = totalNet.divide(new BigDecimal(allSales.size()), 2, RoundingMode.HALF_UP);
        }
        
        // Calculate product sales statistics
        Map<Long, List<Sale>> salesByProduct = allSales.stream()
                .filter(sale -> sale.getIdProduct() != null)
                .collect(Collectors.groupingBy(Sale::getIdProduct));
        
        // Calculate sales info for each product
        List<ProductSalesInfoDto> productSalesInfo = salesByProduct.entrySet().stream()
                .map(entry -> {
                    Long productId = entry.getKey();
                    List<Sale> productSales = entry.getValue();
                    
                    String productName = getProductName(productId);
                    Integer totalQuantitySold = productSales.stream()
                            .mapToInt(sale -> sale.getAmount() != null ? sale.getAmount() : 0)
                            .sum();
                    Integer salesCount = productSales.size();
                    BigDecimal totalRevenue = productSales.stream()
                            .map(sale -> sale.getTotal() != null ? sale.getTotal() : BigDecimal.ZERO)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                    
                    return new ProductSalesInfoDto(productId, productName, totalQuantitySold, salesCount, totalRevenue);
                })
                .collect(Collectors.toList());
        
        // Find most sold product (by total quantity sold)
        ProductSalesInfoDto mostSoldProduct = null;
        if (!productSalesInfo.isEmpty()) {
            mostSoldProduct = productSalesInfo.stream()
                    .max(Comparator.comparing(ProductSalesInfoDto::getTotalQuantitySold))
                    .orElse(null);
        }
        
        // Find least sold products (products with minimum quantity sold)
        List<ProductSalesInfoDto> leastSoldProducts = new ArrayList<>();
        if (!productSalesInfo.isEmpty()) {
            Integer minQuantity = productSalesInfo.stream()
                    .mapToInt(ProductSalesInfoDto::getTotalQuantitySold)
                    .min()
                    .orElse(0);
            
            leastSoldProducts = productSalesInfo.stream()
                    .filter(info -> info.getTotalQuantitySold().equals(minQuantity))
                    .collect(Collectors.toList());
        }
        
        // Calculate daily sales
        Map<LocalDate, List<Sale>> salesByDate = allSales.stream()
                .filter(sale -> sale.getDate() != null)
                .collect(Collectors.groupingBy(sale -> sale.getDate().toLocalDate()));
        
        List<DailySalesDto> dailySales = salesByDate.entrySet().stream()
                .map(entry -> {
                    LocalDate date = entry.getKey();
                    List<Sale> daySales = entry.getValue();
                    
                    Integer salesCount = daySales.size();
                    BigDecimal dayRevenue = daySales.stream()
                            .map(sale -> sale.getTotal() != null ? sale.getTotal() : BigDecimal.ZERO)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                    
                    return new DailySalesDto(date, salesCount, dayRevenue);
                })
                .sorted(Comparator.comparing(DailySalesDto::getDate))
                .collect(Collectors.toList());
        
        // Build and return report
        return new SalesReportResponseDto(
                store.getIdStore(),
                store.getName(),
                store.getAddress(),
                request.getStartDate(),
                request.getEndDate(),
                allSales.size(),
                totalSubtotal,
                totalDiscount,
                totalNet,
                averageSale,
                mostSoldProduct,
                leastSoldProducts,
                dailySales,
                salesDtos
        );
    }
    
    /**
     * Generate sales analysis report (with optional store filter)
     */
    @Transactional(readOnly = true)
    public SalesAnalysisResponseDto generateSalesAnalysis(SalesAnalysisRequestDto request) {
        List<Sale> allSales = new ArrayList<>();
        String storeName = null;
        Long idStore = null;
        
        // If id_store is provided, filter by store
        if (request.getId_store() != null) {
            // Validate store exists
            com.mamukas.erp.erpbackend.domain.entities.Store store = storeService.findById(request.getId_store())
                    .orElseThrow(() -> new IllegalArgumentException("Tienda con ID " + request.getId_store() + " no encontrada"));
            
            storeName = store.getName();
            idStore = store.getIdStore();
            
            // Get all employee-store assignments for this store
            List<com.mamukas.erp.erpbackend.application.dtos.response.EmployeeStoreResponseDto> employeeStores = 
                    employeeStoreService.getEmployeesByStore(request.getId_store());
            
            if (!employeeStores.isEmpty()) {
                // Get all id_employee_store values
                List<Long> employeeStoreIds = employeeStores.stream()
                        .map(com.mamukas.erp.erpbackend.application.dtos.response.EmployeeStoreResponseDto::getIdEmployeeStore)
                        .collect(Collectors.toList());
                
                // Get all sales for these employee stores within date range
                for (Long employeeStoreId : employeeStoreIds) {
                    List<SaleJpaEntity> salesEntities = saleRepository.findByEmployeeStoreAndDateRange(
                            employeeStoreId,
                            request.getStartDate(),
                            request.getEndDate()
                    );
                    allSales.addAll(salesEntities.stream()
                            .map(saleRepository::toDomain)
                            .collect(Collectors.toList()));
                }
            }
        } else {
            // If no id_store, get all sales in date range
            allSales = saleRepository.findByDateBetween(request.getStartDate(), request.getEndDate())
                    .stream()
                    .map(saleRepository::toDomain)
                    .collect(Collectors.toList());
        }
        
        // Convert sales to response DTOs with product and customer names
        List<SaleResponseDto> salesDtos = allSales.stream()
                .map(sale -> {
                    String productName = getProductName(sale.getIdProduct());
                    String customerName = getCustomerName(sale.getIdCustomer());
                    return new SaleResponseDto(
                            sale.getIdSale(),
                            sale.getDate(),
                            sale.getIdProduct(),
                            sale.getIdCustomer(),
                            sale.getIdEmployeeStore(),
                            productName,
                            customerName,
                            sale.getAmount(),
                            sale.getSubtotal(),
                            sale.getDiscount(),
                            sale.getTotal(),
                            sale.getCreatedAt(),
                            sale.getUpdatedAt()
                    );
                })
                .collect(Collectors.toList());
        
        // Calculate product sales statistics
        Map<Long, List<Sale>> salesByProduct = allSales.stream()
                .filter(sale -> sale.getIdProduct() != null)
                .collect(Collectors.groupingBy(Sale::getIdProduct));
        
        // Calculate sales info for each product
        List<ProductSalesInfoDto> productSalesInfo = salesByProduct.entrySet().stream()
                .map(entry -> {
                    Long productId = entry.getKey();
                    List<Sale> productSales = entry.getValue();
                    
                    String productName = getProductName(productId);
                    Integer totalQuantitySold = productSales.stream()
                            .mapToInt(sale -> sale.getAmount() != null ? sale.getAmount() : 0)
                            .sum();
                    Integer salesCount = productSales.size();
                    BigDecimal totalRevenue = productSales.stream()
                            .map(sale -> sale.getTotal() != null ? sale.getTotal() : BigDecimal.ZERO)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                    
                    return new ProductSalesInfoDto(productId, productName, totalQuantitySold, salesCount, totalRevenue);
                })
                .collect(Collectors.toList());
        
        // Find most sold product (by total quantity sold)
        ProductSalesInfoDto mostSoldProduct = null;
        if (!productSalesInfo.isEmpty()) {
            mostSoldProduct = productSalesInfo.stream()
                    .max(Comparator.comparing(ProductSalesInfoDto::getTotalQuantitySold))
                    .orElse(null);
        }
        
        // Find least sold products (products with minimum quantity sold)
        List<ProductSalesInfoDto> leastSoldProducts = new ArrayList<>();
        if (!productSalesInfo.isEmpty()) {
            Integer minQuantity = productSalesInfo.stream()
                    .mapToInt(ProductSalesInfoDto::getTotalQuantitySold)
                    .min()
                    .orElse(0);
            
            leastSoldProducts = productSalesInfo.stream()
                    .filter(info -> info.getTotalQuantitySold().equals(minQuantity))
                    .collect(Collectors.toList());
        }
        
        // Build and return report
        return new SalesAnalysisResponseDto(
                idStore,
                storeName,
                request.getStartDate(),
                request.getEndDate(),
                allSales.size(),
                salesDtos,
                mostSoldProduct,
                leastSoldProducts
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
    
    /**
     * Get customer name by ID
     */
    private String getCustomerName(Long idCustomer) {
        if (idCustomer == null) {
            return "Cliente desconocido";
        }
        return customerService.findById(idCustomer)
                .map(com.mamukas.erp.erpbackend.domain.entities.Customer::getName)
                .orElse("Cliente desconocido");
    }

    /**
     * Map to response DTO
     */
    private SaleResponseDto mapToResponseDto(Sale sale) {
        return new SaleResponseDto(
                sale.getIdSale(),
                sale.getDate(),
                sale.getIdProduct(),
                sale.getIdCustomer(),
                sale.getIdEmployeeStore(),
                sale.getAmount(),
                sale.getSubtotal(),
                sale.getDiscount(),
                sale.getTotal(),
                sale.getCreatedAt(),
                sale.getUpdatedAt()
        );
    }
}
