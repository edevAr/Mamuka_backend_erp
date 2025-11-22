package com.mamukas.erp.erpbackend.infrastructure.web.controller;

import com.mamukas.erp.erpbackend.application.dtos.request.StoreRequestDto;
import com.mamukas.erp.erpbackend.application.dtos.response.MessageResponseDto;
import com.mamukas.erp.erpbackend.application.dtos.response.StoreResponseDto;
import com.mamukas.erp.erpbackend.application.dtos.response.PageResponseDto;
import com.mamukas.erp.erpbackend.application.dtos.response.EmployeeStoreResponseDto;
import com.mamukas.erp.erpbackend.application.services.StoreService;
import com.mamukas.erp.erpbackend.application.services.EmployeeStoreService;
import com.mamukas.erp.erpbackend.application.services.UserService;
import com.mamukas.erp.erpbackend.application.dtos.user.UserResponseDto;
import com.mamukas.erp.erpbackend.domain.entities.Store;
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
@RequestMapping("/api/stores")
@CrossOrigin(origins = "*")
@PreAuthorize("hasRole('USER')")
public class StoreController {
    
    @Autowired
    private StoreService storeService;
    
    @Autowired
    private EmployeeStoreService employeeStoreService;
    
    @Autowired
    private UserService userService;
    
    @PostMapping
    public ResponseEntity<StoreResponseDto> createStore(@Valid @RequestBody StoreRequestDto request) {
        try {
            Store store = storeService.createStore(request.getName(), request.getAddress(), request.getBusinessHours(), request.getIdCompany());
            if (request.getStatus() != null && !request.getStatus().equals("Open")) {
                store = storeService.updateStore(store.getIdStore(), store.getName(), store.getAddress(), request.getStatus(), store.getBusinessHours(), store.getIdCompany());
            }
            
            StoreResponseDto response = convertToResponseDto(store);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
    @PreAuthorize("hasAnyAuthority('STORES_*', 'READ_STORES') or hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<PageResponseDto<StoreResponseDto>> getAllStores(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "idStore") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDir) {
        try {
            Sort sort = sortDir.equalsIgnoreCase("DESC") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
            Pageable pageable = PageRequest.of(page, size, sort);
            
            Page<Store> storePage = storeService.findAll(pageable);
            List<StoreResponseDto> content = storePage.getContent().stream()
                    .map(this::convertToResponseDto)
                    .collect(Collectors.toList());
            
            PageResponseDto<StoreResponseDto> response = new PageResponseDto<>(
                content,
                storePage.getTotalElements(),
                storePage.getTotalPages(),
                storePage.getNumber(),
                storePage.getSize(),
                storePage.hasNext(),
                storePage.hasPrevious()
            );
            
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<StoreResponseDto> getStoreById(@PathVariable Long id) {
        try {
            Optional<Store> store = storeService.findById(id);
            if (store.isPresent()) {
                StoreResponseDto response = convertToResponseDto(store.get());
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("/open")
    public ResponseEntity<List<StoreResponseDto>> getOpenStores() {
        try {
            List<Store> stores = storeService.findOpenStores();
            List<StoreResponseDto> response = stores.stream()
                    .map(this::convertToResponseDto)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("/closed")
    public ResponseEntity<List<StoreResponseDto>> getClosedStores() {
        try {
            List<Store> stores = storeService.findClosedStores();
            List<StoreResponseDto> response = stores.stream()
                    .map(this::convertToResponseDto)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("/status/{status}")
    public ResponseEntity<List<StoreResponseDto>> getStoresByStatus(@PathVariable String status) {
        try {
            List<Store> stores = storeService.findByStatus(status);
            List<StoreResponseDto> response = stores.stream()
                    .map(this::convertToResponseDto)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("/search/name")
    public ResponseEntity<List<StoreResponseDto>> searchStoresByName(@RequestParam String name) {
        try {
            List<Store> stores = storeService.findByNameContaining(name);
            List<StoreResponseDto> response = stores.stream()
                    .map(this::convertToResponseDto)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("/search/address")
    public ResponseEntity<List<StoreResponseDto>> searchStoresByAddress(@RequestParam String address) {
        try {
            List<Store> stores = storeService.findByAddressContaining(address);
            List<StoreResponseDto> response = stores.stream()
                    .map(this::convertToResponseDto)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PreAuthorize("hasAuthority('STORES_*') or hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<StoreResponseDto> updateStore(@PathVariable Long id, @Valid @RequestBody StoreRequestDto request) {
        try {
            Store store = storeService.updateStore(id, request.getName(), request.getAddress(), request.getStatus(), request.getBusinessHours(), request.getIdCompany());
            StoreResponseDto response = convertToResponseDto(store);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
    
    @PutMapping("/{id}/open")
    public ResponseEntity<MessageResponseDto> openStore(@PathVariable Long id) {
        try {
            storeService.openStore(id);
            return new ResponseEntity<>(new MessageResponseDto("Store opened successfully"), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(new MessageResponseDto("Store not found"), HttpStatus.NOT_FOUND);
        }
    }
    
    @PutMapping("/{id}/close")
    public ResponseEntity<MessageResponseDto> closeStore(@PathVariable Long id) {
        try {
            storeService.closeStore(id);
            return new ResponseEntity<>(new MessageResponseDto("Store closed successfully"), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(new MessageResponseDto("Store not found"), HttpStatus.NOT_FOUND);
        }
    }
    
    @PreAuthorize("hasAuthority('STORES_*') or hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponseDto> deleteStore(@PathVariable Long id) {
        try {
            storeService.deleteStore(id);
            return new ResponseEntity<>(new MessageResponseDto("Store deleted successfully"), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(new MessageResponseDto("Store not found"), HttpStatus.NOT_FOUND);
        }
    }
    
    private StoreResponseDto convertToResponseDto(Store store) {
        // Get name (from users.name column) of first employee linked to this store
        String username = getUsernameForStore(store.getIdStore());
        
        return new StoreResponseDto(
            store.getIdStore(),
            store.getName(),
            store.getAddress(),
            store.getStatus(),
            store.getBusinessHours(),
            store.getIdCompany(),
            store.getRate(),
            store.getCreatedAt(),
            store.getUpdatedAt(),
            username
        );
    }
    
    private String getUsernameForStore(Long storeId) {
        try {
            // Get all employee-store assignments for this store
            List<EmployeeStoreResponseDto> assignments = employeeStoreService.getEmployeesByStore(storeId);
            
            // Get the name and last_name (from users.name and users.last_name columns) of the first employee linked
            if (!assignments.isEmpty()) {
                try {
                    UserResponseDto user = userService.getUserById(assignments.get(0).getIdEmployee());
                    // Concatenate name and lastName with a space: "Eden Guzman"
                    String name = user.getName() != null ? user.getName() : "";
                    String lastName = user.getLastName() != null ? user.getLastName() : "";
                    return (name + " " + lastName).trim(); // trim() removes extra spaces if one is null
                } catch (Exception e) {
                    // If user not found, return null
                    return null;
                }
            }
            return null;
        } catch (Exception e) {
            // If there's an error, return null
            return null;
        }
    }
}
