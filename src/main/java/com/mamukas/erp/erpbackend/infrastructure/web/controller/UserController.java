package com.mamukas.erp.erpbackend.infrastructure.web.controller;

import com.mamukas.erp.erpbackend.application.dtos.user.UserRequestDto;
import com.mamukas.erp.erpbackend.application.dtos.user.UserResponseDto;
import com.mamukas.erp.erpbackend.application.services.UserService;
import com.mamukas.erp.erpbackend.application.dtos.response.PageResponseDto;
import com.mamukas.erp.erpbackend.application.dtos.response.UserWithRolesAndPermissionsResponseDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

/**
 * REST Controller for User operations
 * This controller handles HTTP requests and delegates to the UserService
 */
@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {
    
    private final UserService userService;
    
    public UserController(UserService userService) {
        this.userService = userService;
    }
    
    /**
     * Create a new user
     * POST /api/users
     */
    @PostMapping
    @PreAuthorize("hasAuthority('USER_MANAGEMENT_CREATE')")
    public ResponseEntity<ApiResponse<UserResponseDto>> createUser(@Valid @RequestBody UserRequestDto userRequestDto) {
        try {
            // Validate required fields for creation
            userRequestDto.validateForCreation();
            
            UserResponseDto createdUser = userService.createUser(userRequestDto);
            ApiResponse<UserResponseDto> response = new ApiResponse<>(
                true,
                "User created successfully",
                createdUser
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            ApiResponse<UserResponseDto> response = new ApiResponse<>(
                false,
                e.getMessage(),
                null
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            ApiResponse<UserResponseDto> response = new ApiResponse<>(
                false,
                "Internal server error occurred",
                null
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Get all users with pagination
     * GET /api/users
     */
    @GetMapping
    @PreAuthorize("hasAuthority('USER_*')")
    public ResponseEntity<ApiResponse<PageResponseDto<UserResponseDto>>> getAllUsers(
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "idUser") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDir) {
        try {
            Sort sort = sortDir.equalsIgnoreCase("DESC") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
            Pageable pageable = PageRequest.of(page, size, sort);
            
            Page<UserResponseDto> userPage;
            if (status != null) {
                // For status filtering, we need to get all and filter, then paginate manually
                // This is not ideal but works with current service structure
                List<UserResponseDto> allUsers = userService.getUsersByStatus(status);
                int start = (int) pageable.getOffset();
                int end = Math.min((start + pageable.getPageSize()), allUsers.size());
                List<UserResponseDto> pageContent = allUsers.subList(start, end);
                
                PageResponseDto<UserResponseDto> pageResponse = new PageResponseDto<>(
                    pageContent,
                    allUsers.size(),
                    (int) Math.ceil((double) allUsers.size() / size),
                    page,
                    size,
                    end < allUsers.size(),
                    page > 0
                );
                
                ApiResponse<PageResponseDto<UserResponseDto>> response = new ApiResponse<>(
                    true,
                    "Users retrieved successfully",
                    pageResponse
                );
                return ResponseEntity.ok(response);
            } else {
                userPage = userService.getAllUsers(pageable);
                List<UserResponseDto> content = userPage.getContent();
                
                PageResponseDto<UserResponseDto> pageResponse = new PageResponseDto<>(
                    content,
                    userPage.getTotalElements(),
                    userPage.getTotalPages(),
                    userPage.getNumber(),
                    userPage.getSize(),
                    userPage.hasNext(),
                    userPage.hasPrevious()
                );
                
                ApiResponse<PageResponseDto<UserResponseDto>> response = new ApiResponse<>(
                    true,
                    "Users retrieved successfully",
                    pageResponse
                );
                return ResponseEntity.ok(response);
            }
        } catch (Exception e) {
            ApiResponse<PageResponseDto<UserResponseDto>> response = new ApiResponse<>(
                false,
                "Error retrieving users: " + e.getMessage(),
                null
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Get user by ID with roles and permissions
     * GET /api/users/{id}/details
     */
    @GetMapping("/{id}/details")
    @PreAuthorize("hasAuthority('USER_*') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UserWithRolesAndPermissionsResponseDto>> getUserWithRolesAndPermissions(@PathVariable Long id) {
        try {
            UserWithRolesAndPermissionsResponseDto userDetails = userService.getUserWithRolesAndPermissions(id);
            ApiResponse<UserWithRolesAndPermissionsResponseDto> response = new ApiResponse<>(
                true,
                "User details retrieved successfully",
                userDetails
            );
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            ApiResponse<UserWithRolesAndPermissionsResponseDto> response = new ApiResponse<>(
                false,
                e.getMessage(),
                null
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            ApiResponse<UserWithRolesAndPermissionsResponseDto> response = new ApiResponse<>(
                false,
                "Error retrieving user details: " + e.getMessage(),
                null
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Get user by ID
     * GET /api/users/{id}
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('USER_MANAGEMENT_READ')")
    public ResponseEntity<ApiResponse<UserResponseDto>> getUserById(@PathVariable Long id) {
        try {
            UserResponseDto user = userService.getUserById(id);
            ApiResponse<UserResponseDto> response = new ApiResponse<>(
                true,
                "User retrieved successfully",
                user
            );
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            ApiResponse<UserResponseDto> response = new ApiResponse<>(
                false,
                e.getMessage(),
                null
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            ApiResponse<UserResponseDto> response = new ApiResponse<>(
                false,
                "Error retrieving user",
                null
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Get user by username
     * GET /api/users/username/{username}
     */
    @GetMapping("/username/{username}")
    @PreAuthorize("hasAuthority('USER_MANAGEMENT_READ')")
    public ResponseEntity<ApiResponse<UserResponseDto>> getUserByUsername(@PathVariable String username) {
        try {
            UserResponseDto user = userService.getUserByUsername(username);
            ApiResponse<UserResponseDto> response = new ApiResponse<>(
                true,
                "User retrieved successfully",
                user
            );
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            ApiResponse<UserResponseDto> response = new ApiResponse<>(
                false,
                e.getMessage(),
                null
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            ApiResponse<UserResponseDto> response = new ApiResponse<>(
                false,
                "Error retrieving user",
                null
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Update user
     * PUT /api/users/{id}
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('USER_MANAGEMENT_UPDATE')")
    public ResponseEntity<ApiResponse<UserResponseDto>> updateUser(
            @PathVariable Long id, 
            @Valid @RequestBody UserRequestDto userRequestDto) {
        try {
            // Validate that at least one field is provided for update
            userRequestDto.validateForUpdate();
            
            UserResponseDto updatedUser = userService.updateUser(id, userRequestDto);
            ApiResponse<UserResponseDto> response = new ApiResponse<>(
                true,
                "User updated successfully",
                updatedUser
            );
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            ApiResponse<UserResponseDto> response = new ApiResponse<>(
                false,
                e.getMessage(),
                null
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            ApiResponse<UserResponseDto> response = new ApiResponse<>(
                false,
                "Error updating user",
                null
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Delete user (hard delete)
     * DELETE /api/users/{id}
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('USER_MANAGEMENT_DELETE')")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long id) {
        try {
            boolean deleted = userService.deleteUser(id);
            if (deleted) {
                ApiResponse<Void> response = new ApiResponse<>(
                    true,
                    "User deleted successfully",
                    null
                );
                return ResponseEntity.ok(response);
            } else {
                ApiResponse<Void> response = new ApiResponse<>(
                    false,
                    "Failed to delete user",
                    null
                );
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
        } catch (IllegalArgumentException e) {
            ApiResponse<Void> response = new ApiResponse<>(
                false,
                e.getMessage(),
                null
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            ApiResponse<Void> response = new ApiResponse<>(
                false,
                "Error deleting user",
                null
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Deactivate user (soft delete)
     * PATCH /api/users/{id}/deactivate
     */
    @PatchMapping("/{id}/deactivate")
    @PreAuthorize("hasAuthority('USER_MANAGEMENT_UPDATE')")
    public ResponseEntity<ApiResponse<UserResponseDto>> deactivateUser(@PathVariable Long id) {
        try {
            UserResponseDto deactivatedUser = userService.deactivateUser(id);
            ApiResponse<UserResponseDto> response = new ApiResponse<>(
                true,
                "User deactivated successfully",
                deactivatedUser
            );
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            ApiResponse<UserResponseDto> response = new ApiResponse<>(
                false,
                e.getMessage(),
                null
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            ApiResponse<UserResponseDto> response = new ApiResponse<>(
                false,
                "Error deactivating user",
                null
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Activate user
     * PATCH /api/users/{id}/activate
     */
    @PatchMapping("/{id}/activate")
    @PreAuthorize("hasAuthority('USER_MANAGEMENT_UPDATE')")
    public ResponseEntity<ApiResponse<UserResponseDto>> activateUser(@PathVariable Long id) {
        try {
            UserResponseDto activatedUser = userService.activateUser(id);
            ApiResponse<UserResponseDto> response = new ApiResponse<>(
                true,
                "User activated successfully",
                activatedUser
            );
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            ApiResponse<UserResponseDto> response = new ApiResponse<>(
                false,
                e.getMessage(),
                null
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            ApiResponse<UserResponseDto> response = new ApiResponse<>(
                false,
                "Error activating user",
                null
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * API Response wrapper class
     */
    public static class ApiResponse<T> {
        private boolean success;
        private String message;
        private T data;
        
        public ApiResponse(boolean success, String message, T data) {
            this.success = success;
            this.message = message;
            this.data = data;
        }
        
        // Getters and setters
        public boolean isSuccess() { return success; }
        public void setSuccess(boolean success) { this.success = success; }
        
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        
        public T getData() { return data; }
        public void setData(T data) { this.data = data; }
    }
}
