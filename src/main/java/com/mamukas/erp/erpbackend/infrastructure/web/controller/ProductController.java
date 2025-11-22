package com.mamukas.erp.erpbackend.infrastructure.web.controller;

import com.mamukas.erp.erpbackend.application.dtos.request.ProductRequestDto;
import com.mamukas.erp.erpbackend.application.dtos.response.MessageResponseDto;
import com.mamukas.erp.erpbackend.application.dtos.response.ProductResponseDto;
import com.mamukas.erp.erpbackend.application.dtos.response.PageResponseDto;
import com.mamukas.erp.erpbackend.application.services.ProductService;
import com.mamukas.erp.erpbackend.domain.entities.Product;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*")
public class ProductController {
    
    @Autowired
    private ProductService productService;
    
    @PreAuthorize("hasAuthority('INVENTORY_CREATE') or hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ProductResponseDto> createProduct(@Valid @RequestBody ProductRequestDto request) {
        try {
            Product product = productService.createProduct(
                request.getName(),
                request.getStatus(),
                request.getPrice(),
                request.getExpirationDate(),
                request.getDescripcion()
            );
            ProductResponseDto response = convertToResponseDto(product);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
    
    @PreAuthorize("hasAnyAuthority('PRODUCTS_*', 'READ_PRODUCTS') or hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<PageResponseDto<ProductResponseDto>> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "idProduct") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDir) {
        try {
            Sort sort = sortDir.equalsIgnoreCase("DESC") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
            Pageable pageable = PageRequest.of(page, size, sort);
            
            Page<Product> productPage = productService.findAll(pageable);
            
            // Get stock for all products in batch (optimized for performance)
            List<Long> productIds = productPage.getContent().stream()
                    .map(Product::getIdProduct)
                    .collect(Collectors.toList());
            java.util.Map<Long, Integer> stockMap = productService.getProductStockBatch(productIds);
            
            // Convert to DTOs with stock from boxes/packs
            List<ProductResponseDto> content = productPage.getContent().stream()
                    .map(product -> convertToResponseDto(product, stockMap.getOrDefault(product.getIdProduct(), 0)))
                    .collect(Collectors.toList());
            
            PageResponseDto<ProductResponseDto> response = new PageResponseDto<>(
                content,
                productPage.getTotalElements(),
                productPage.getTotalPages(),
                productPage.getNumber(),
                productPage.getSize(),
                productPage.hasNext(),
                productPage.hasPrevious()
            );
            
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDto> getProductById(@PathVariable Long id) {
        try {
            Optional<Product> product = productService.findById(id);
            if (product.isPresent()) {
                ProductResponseDto response = convertToResponseDto(product.get());
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("/active")
    public ResponseEntity<List<ProductResponseDto>> getActiveProducts() {
        try {
            List<Product> products = productService.findActiveProducts();
            List<ProductResponseDto> response = products.stream()
                    .map(this::convertToResponseDto)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("/inactive")
    public ResponseEntity<List<ProductResponseDto>> getInactiveProducts() {
        try {
            List<Product> products = productService.findInactiveProducts();
            List<ProductResponseDto> response = products.stream()
                    .map(this::convertToResponseDto)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("/status/{status}")
    public ResponseEntity<List<ProductResponseDto>> getProductsByStatus(@PathVariable String status) {
        try {
            List<Product> products = productService.findByStatus(status);
            List<ProductResponseDto> response = products.stream()
                    .map(this::convertToResponseDto)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("/search/name")
    public ResponseEntity<List<ProductResponseDto>> searchProductsByName(@RequestParam String name) {
        try {
            List<Product> products = productService.findByNameContaining(name);
            List<ProductResponseDto> response = products.stream()
                    .map(this::convertToResponseDto)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("/search/price")
    public ResponseEntity<List<ProductResponseDto>> searchProductsByPriceRange(
            @RequestParam BigDecimal minPrice,
            @RequestParam BigDecimal maxPrice) {
        try {
            List<Product> products = productService.findByPriceRange(minPrice, maxPrice);
            List<ProductResponseDto> response = products.stream()
                    .map(this::convertToResponseDto)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("/search/max-price")
    public ResponseEntity<List<ProductResponseDto>> searchActiveProductsByMaxPrice(@RequestParam BigDecimal maxPrice) {
        try {
            List<Product> products = productService.findActiveProductsByMaxPrice(maxPrice);
            List<ProductResponseDto> response = products.stream()
                    .map(this::convertToResponseDto)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("/expiring/before")
    public ResponseEntity<List<ProductResponseDto>> getProductsExpiringBefore(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        try {
            List<Product> products = productService.findExpiringBefore(date);
            List<ProductResponseDto> response = products.stream()
                    .map(this::convertToResponseDto)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("/expiring/soon")
    public ResponseEntity<List<ProductResponseDto>> getProductsExpiringSoon(@RequestParam(defaultValue = "30") int days) {
        try {
            List<Product> products = productService.findExpiringSoon(days);
            List<ProductResponseDto> response = products.stream()
                    .map(this::convertToResponseDto)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("/expiring/between")
    public ResponseEntity<List<ProductResponseDto>> getProductsExpiringBetween(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        try {
            List<Product> products = productService.findExpiringBetween(startDate, endDate);
            List<ProductResponseDto> response = products.stream()
                    .map(this::convertToResponseDto)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDto> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductRequestDto request) {
        try {
            Product product = productService.updateProduct(
                id,
                request.getName(),
                request.getStatus(),
                request.getPrice(),
                request.getExpirationDate(),
                request.getDescripcion()
            );
            ProductResponseDto response = convertToResponseDto(product);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
    
    @PutMapping("/{id}/activate")
    public ResponseEntity<MessageResponseDto> activateProduct(@PathVariable Long id) {
        try {
            productService.activateProduct(id);
            return new ResponseEntity<>(new MessageResponseDto("Product activated successfully"), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(new MessageResponseDto("Product not found"), HttpStatus.NOT_FOUND);
        }
    }
    
    @PutMapping("/{id}/deactivate")
    public ResponseEntity<MessageResponseDto> deactivateProduct(@PathVariable Long id) {
        try {
            productService.deactivateProduct(id);
            return new ResponseEntity<>(new MessageResponseDto("Product deactivated successfully"), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(new MessageResponseDto("Product not found"), HttpStatus.NOT_FOUND);
        }
    }
    
    @GetMapping("/count/status/{status}")
    public ResponseEntity<Long> countProductsByStatus(@PathVariable String status) {
        try {
            long count = productService.countByStatus(status);
            return new ResponseEntity<>(count, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponseDto> deleteProduct(@PathVariable Long id) {
        try {
            productService.deleteProduct(id);
            return new ResponseEntity<>(new MessageResponseDto("Product deleted successfully"), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(new MessageResponseDto("Product not found"), HttpStatus.NOT_FOUND);
        }
    }
    
    private ProductResponseDto convertToResponseDto(Product product) {
        // Get stock from boxes or packs (first one found)
        Integer stock = productService.getProductStock(product.getIdProduct());
        
        return convertToResponseDto(product, stock);
    }
    
    private ProductResponseDto convertToResponseDto(Product product, Integer stock) {
        return new ProductResponseDto(
            product.getIdProduct(),
            product.getName(),
            product.getStatus(),
            product.getPrice(),
            product.getExpirationDate(),
            stock, // Use calculated stock
            product.getDescripcion(),
            product.getCreatedAt(),
            product.getUpdatedAt()
        );
    }
}
