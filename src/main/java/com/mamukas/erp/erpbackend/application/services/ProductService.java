package com.mamukas.erp.erpbackend.application.services;

import com.mamukas.erp.erpbackend.domain.entities.Product;
import com.mamukas.erp.erpbackend.domain.entities.Box;
import com.mamukas.erp.erpbackend.domain.entities.Pack;
import com.mamukas.erp.erpbackend.infrastructure.persistence.jpa.ProductJpaEntity;
import com.mamukas.erp.erpbackend.infrastructure.repositories.ProductRepository;
import com.mamukas.erp.erpbackend.infrastructure.repositories.BoxRepository;
import com.mamukas.erp.erpbackend.infrastructure.repositories.PackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
@Transactional
public class ProductService {
    
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private BoxRepository boxRepository;
    
    @Autowired
    private PackRepository packRepository;
    
    @Autowired
    private BoxService boxService;
    
    @Autowired
    private PackService packService;
    
    /**
     * Save product
     */
    public Product save(Product product) {
        ProductJpaEntity entity = productRepository.toEntity(product);
        ProductJpaEntity savedEntity = productRepository.save(entity);
        return productRepository.toDomain(savedEntity);
    }
    
    /**
     * Create new product
     */
    public Product createProduct(String name, String status, BigDecimal price, LocalDate expirationDate) {
        Product product = new Product(name, status, price, expirationDate);
        return save(product);
    }
    
    /**
     * Create new product with description
     */
    public Product createProduct(String name, String status, BigDecimal price, LocalDate expirationDate, String descripcion) {
        Product product = new Product(name, status, price, expirationDate, descripcion);
        return save(product);
    }
    
    /**
     * Find product by ID
     */
    @Transactional(readOnly = true)
    public Optional<Product> findById(Long id) {
        Optional<ProductJpaEntity> entity = productRepository.findByIdProduct(id);
        return entity.map(productRepository::toDomain);
    }
    
    /**
     * Find all products
     */
    @Transactional(readOnly = true)
    public List<Product> findAll() {
        return productRepository.findAll()
                .stream()
                .map(productRepository::toDomain)
                .collect(Collectors.toList());
    }
    
    /**
     * Find all products with pagination
     */
    @Transactional(readOnly = true)
    public Page<Product> findAll(Pageable pageable) {
        return productRepository.findAll(pageable)
                .map(productRepository::toDomain);
    }
    
    /**
     * Find products by status
     */
    @Transactional(readOnly = true)
    public List<Product> findByStatus(String status) {
        return productRepository.findByStatus(status)
                .stream()
                .map(productRepository::toDomain)
                .collect(Collectors.toList());
    }
    
    /**
     * Find active products
     */
    @Transactional(readOnly = true)
    public List<Product> findActiveProducts() {
        return productRepository.findActiveProducts()
                .stream()
                .map(productRepository::toDomain)
                .collect(Collectors.toList());
    }
    
    /**
     * Find inactive products
     */
    @Transactional(readOnly = true)
    public List<Product> findInactiveProducts() {
        return productRepository.findInactiveProducts()
                .stream()
                .map(productRepository::toDomain)
                .collect(Collectors.toList());
    }
    
    /**
     * Find products by name containing text
     */
    @Transactional(readOnly = true)
    public List<Product> findByNameContaining(String name) {
        return productRepository.findByNameContaining(name)
                .stream()
                .map(productRepository::toDomain)
                .collect(Collectors.toList());
    }
    
    /**
     * Find product by exact name
     */
    @Transactional(readOnly = true)
    public Optional<Product> findByName(String name) {
        Optional<ProductJpaEntity> entity = productRepository.findByName(name);
        return entity.map(productRepository::toDomain);
    }
    
    /**
     * Find products by price range
     */
    @Transactional(readOnly = true)
    public List<Product> findByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        return productRepository.findByPriceBetween(minPrice, maxPrice)
                .stream()
                .map(productRepository::toDomain)
                .collect(Collectors.toList());
    }
    
    /**
     * Find products by max price (active only)
     */
    @Transactional(readOnly = true)
    public List<Product> findActiveProductsByMaxPrice(BigDecimal maxPrice) {
        return productRepository.findActiveProductsByMaxPrice(maxPrice)
                .stream()
                .map(productRepository::toDomain)
                .collect(Collectors.toList());
    }
    
    /**
     * Find products expiring before a date
     */
    @Transactional(readOnly = true)
    public List<Product> findExpiringBefore(LocalDate date) {
        return productRepository.findByExpirationDateBefore(date)
                .stream()
                .map(productRepository::toDomain)
                .collect(Collectors.toList());
    }
    
    /**
     * Find products expiring after a date
     */
    @Transactional(readOnly = true)
    public List<Product> findExpiringAfter(LocalDate date) {
        return productRepository.findByExpirationDateAfter(date)
                .stream()
                .map(productRepository::toDomain)
                .collect(Collectors.toList());
    }
    
    /**
     * Find products expiring in date range
     */
    @Transactional(readOnly = true)
    public List<Product> findExpiringBetween(LocalDate startDate, LocalDate endDate) {
        return productRepository.findByExpirationDateBetween(startDate, endDate)
                .stream()
                .map(productRepository::toDomain)
                .collect(Collectors.toList());
    }
    
    /**
     * Find products expiring soon (within specified days)
     */
    @Transactional(readOnly = true)
    public List<Product> findExpiringSoon(int days) {
        LocalDate futureDate = LocalDate.now().plusDays(days);
        return findExpiringBefore(futureDate);
    }
    
    /**
     * Update product
     */
    public Product updateProduct(Long id, String name, String status, BigDecimal price, LocalDate expirationDate) {
        Optional<ProductJpaEntity> entityOpt = productRepository.findByIdProduct(id);
        if (entityOpt.isPresent()) {
            Product product = productRepository.toDomain(entityOpt.get());
            product.setName(name);
            product.setStatus(status);
            product.setPrice(price);
            product.setExpirationDate(expirationDate);
            return save(product);
        }
        throw new RuntimeException("Product not found with id: " + id);
    }
    
    /**
     * Update product with description
     */
    public Product updateProduct(Long id, String name, String status, BigDecimal price, LocalDate expirationDate, String descripcion) {
        Optional<ProductJpaEntity> entityOpt = productRepository.findByIdProduct(id);
        if (entityOpt.isPresent()) {
            Product product = productRepository.toDomain(entityOpt.get());
            product.setName(name);
            product.setStatus(status);
            product.setPrice(price);
            product.setExpirationDate(expirationDate);
            product.setDescripcion(descripcion);
            return save(product);
        }
        throw new RuntimeException("Product not found with id: " + id);
    }
    
    /**
     * Activate product
     */
    public Product activateProduct(Long id) {
        Optional<ProductJpaEntity> entityOpt = productRepository.findByIdProduct(id);
        if (entityOpt.isPresent()) {
            Product product = productRepository.toDomain(entityOpt.get());
            product.activate();
            return save(product);
        }
        throw new RuntimeException("Product not found with id: " + id);
    }
    
    /**
     * Deactivate product
     */
    public Product deactivateProduct(Long id) {
        Optional<ProductJpaEntity> entityOpt = productRepository.findByIdProduct(id);
        if (entityOpt.isPresent()) {
            Product product = productRepository.toDomain(entityOpt.get());
            product.deactivate();
            return save(product);
        }
        throw new RuntimeException("Product not found with id: " + id);
    }
    
    /**
     * Count products by status
     */
    @Transactional(readOnly = true)
    public long countByStatus(String status) {
        return productRepository.countByStatus(status);
    }
    
    /**
     * Check if product exists by ID
     */
    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        return productRepository.existsById(id);
    }
    
    /**
     * Delete product
     */
    public void deleteProduct(Long id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
        } else {
            throw new RuntimeException("Product not found with id: " + id);
        }
    }
    
    /**
     * Get stock for a product from boxes or packs tables
     * Simply reads the stock column value from the first box or pack found where id_product matches
     * No calculation, no sum - just read the stock value directly
     * 
     * @param idProduct the product ID
     * @return stock value from boxes or packs, or 0 if not found
     */
    @Transactional(readOnly = true)
    public Integer getProductStock(Long idProduct) {
        if (idProduct == null) {
            return 0;
        }
        
        // First, try to find in boxes
        List<Box> boxes = boxService.findByIdProduct(idProduct);
        if (!boxes.isEmpty()) {
            Box box = boxes.get(0); // Get first box found
            if (box.getStock() != null) {
                return box.getStock();
            }
        }
        
        // If not found in boxes, try to find in packs
        List<Pack> packs = packService.findByIdProduct(idProduct);
        if (!packs.isEmpty()) {
            Pack pack = packs.get(0); // Get first pack found
            if (pack.getStock() != null) {
                return pack.getStock();
            }
        }
        
        // If not found in either, return 0
        return 0;
    }
    
    /**
     * Get stock for multiple products in batch (optimized for pagination)
     * Simply reads the stock column value from the first box or pack found for each product
     * No calculation, no sum - just read the stock value directly
     * 
     * @param productIds list of product IDs
     * @return map of product ID to stock value
     */
    @Transactional(readOnly = true)
    public java.util.Map<Long, Integer> getProductStockBatch(java.util.List<Long> productIds) {
        if (productIds == null || productIds.isEmpty()) {
            return new java.util.HashMap<>();
        }
        
        java.util.Map<Long, Integer> stockMap = new java.util.HashMap<>();
        
        // Initialize all products with 0 stock
        for (Long id : productIds) {
            stockMap.put(id, 0);
        }
        
        // Get all boxes for these products
        for (Long productId : productIds) {
            List<Box> boxes = boxService.findByIdProduct(productId);
            if (!boxes.isEmpty()) {
                Box box = boxes.get(0); // Get first box found
                if (box.getStock() != null) {
                    stockMap.put(productId, box.getStock());
                    continue; // Found in boxes, skip packs
                }
            }
            
            // If not found in boxes, try packs
            List<Pack> packs = packService.findByIdProduct(productId);
            if (!packs.isEmpty()) {
                Pack pack = packs.get(0); // Get first pack found
                if (pack.getStock() != null) {
                    stockMap.put(productId, pack.getStock());
                }
            }
        }
        
        return stockMap;
    }
}
