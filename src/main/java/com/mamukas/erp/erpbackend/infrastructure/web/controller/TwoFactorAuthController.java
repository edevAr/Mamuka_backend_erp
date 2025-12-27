package com.mamukas.erp.erpbackend.infrastructure.web.controller;

import com.mamukas.erp.erpbackend.application.services.TwoFactorAuthService;
import com.mamukas.erp.erpbackend.domain.entities.User;
import com.mamukas.erp.erpbackend.infrastructure.web.controller.UserController.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for Two-Factor Authentication operations
 */
@RestController
@RequestMapping("/api/2fa")
@CrossOrigin(origins = "*")
public class TwoFactorAuthController {
    
    private final TwoFactorAuthService twoFactorAuthService;
    
    public TwoFactorAuthController(TwoFactorAuthService twoFactorAuthService) {
        this.twoFactorAuthService = twoFactorAuthService;
    }
    
    /**
     * Get current authenticated user ID
     */
    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof User)) {
            throw new IllegalStateException("User not authenticated");
        }
        User user = (User) authentication.getPrincipal();
        return user.getIdUser();
    }
    
    /**
     * Setup 2FA - Generate secret and QR code
     * POST /api/2fa/setup
     */
    @PostMapping("/setup")
    public ResponseEntity<ApiResponse<TwoFactorAuthService.TwoFactorSetupResponse>> setupTwoFactor() {
        try {
            Long userId = getCurrentUserId();
            TwoFactorAuthService.TwoFactorSetupResponse response = twoFactorAuthService.setupTwoFactor(userId);
            
            ApiResponse<TwoFactorAuthService.TwoFactorSetupResponse> apiResponse = new ApiResponse<>(
                true,
                "2FA setup initiated. Scan the QR code with your authenticator app.",
                response
            );
            return ResponseEntity.ok(apiResponse);
        } catch (IllegalStateException e) {
            ApiResponse<TwoFactorAuthService.TwoFactorSetupResponse> apiResponse = new ApiResponse<>(
                false,
                e.getMessage(),
                null
            );
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apiResponse);
        } catch (IllegalArgumentException e) {
            ApiResponse<TwoFactorAuthService.TwoFactorSetupResponse> apiResponse = new ApiResponse<>(
                false,
                e.getMessage(),
                null
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiResponse);
        } catch (Exception e) {
            ApiResponse<TwoFactorAuthService.TwoFactorSetupResponse> apiResponse = new ApiResponse<>(
                false,
                "Error setting up 2FA: " + e.getMessage(),
                null
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponse);
        }
    }
    
    /**
     * Enable 2FA - Verify code and enable
     * POST /api/2fa/enable
     */
    @PostMapping("/enable")
    public ResponseEntity<ApiResponse<String>> enableTwoFactor(@Valid @RequestBody EnableTwoFactorRequest request) {
        try {
            Long userId = getCurrentUserId();
            twoFactorAuthService.enableTwoFactor(userId, request.getCode());
            
            ApiResponse<String> apiResponse = new ApiResponse<>(
                true,
                "2FA enabled successfully",
                null
            );
            return ResponseEntity.ok(apiResponse);
        } catch (IllegalStateException e) {
            ApiResponse<String> apiResponse = new ApiResponse<>(
                false,
                e.getMessage(),
                null
            );
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apiResponse);
        } catch (IllegalArgumentException e) {
            ApiResponse<String> apiResponse = new ApiResponse<>(
                false,
                e.getMessage(),
                null
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
        } catch (Exception e) {
            ApiResponse<String> apiResponse = new ApiResponse<>(
                false,
                "Error enabling 2FA: " + e.getMessage(),
                null
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponse);
        }
    }
    
    /**
     * Disable 2FA
     * POST /api/2fa/disable
     */
    @PostMapping("/disable")
    public ResponseEntity<ApiResponse<String>> disableTwoFactor(@Valid @RequestBody DisableTwoFactorRequest request) {
        try {
            Long userId = getCurrentUserId();
            twoFactorAuthService.disableTwoFactor(userId, request.getCode());
            
            ApiResponse<String> apiResponse = new ApiResponse<>(
                true,
                "2FA disabled successfully",
                null
            );
            return ResponseEntity.ok(apiResponse);
        } catch (IllegalStateException e) {
            ApiResponse<String> apiResponse = new ApiResponse<>(
                false,
                e.getMessage(),
                null
            );
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apiResponse);
        } catch (IllegalArgumentException e) {
            ApiResponse<String> apiResponse = new ApiResponse<>(
                false,
                e.getMessage(),
                null
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
        } catch (Exception e) {
            ApiResponse<String> apiResponse = new ApiResponse<>(
                false,
                "Error disabling 2FA: " + e.getMessage(),
                null
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponse);
        }
    }
    
    /**
     * Check if 2FA is enabled for current user
     * GET /api/2fa/status
     */
    @GetMapping("/status")
    public ResponseEntity<ApiResponse<TwoFactorStatusResponse>> getTwoFactorStatus() {
        try {
            Long userId = getCurrentUserId();
            boolean isEnabled = twoFactorAuthService.isTwoFactorEnabled(userId);
            
            TwoFactorStatusResponse statusResponse = new TwoFactorStatusResponse(isEnabled);
            ApiResponse<TwoFactorStatusResponse> apiResponse = new ApiResponse<>(
                true,
                "2FA status retrieved successfully",
                statusResponse
            );
            return ResponseEntity.ok(apiResponse);
        } catch (IllegalStateException e) {
            ApiResponse<TwoFactorStatusResponse> apiResponse = new ApiResponse<>(
                false,
                e.getMessage(),
                null
            );
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apiResponse);
        } catch (Exception e) {
            ApiResponse<TwoFactorStatusResponse> apiResponse = new ApiResponse<>(
                false,
                "Error retrieving 2FA status: " + e.getMessage(),
                null
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponse);
        }
    }
    
    // Request DTOs
    public static class EnableTwoFactorRequest {
        @NotNull(message = "Code is required")
        private Integer code;
        
        public Integer getCode() {
            return code;
        }
        
        public void setCode(Integer code) {
            this.code = code;
        }
    }
    
    public static class DisableTwoFactorRequest {
        @NotNull(message = "Code is required")
        private Integer code;
        
        public Integer getCode() {
            return code;
        }
        
        public void setCode(Integer code) {
            this.code = code;
        }
    }
    
    public static class TwoFactorStatusResponse {
        private boolean enabled;
        
        public TwoFactorStatusResponse(boolean enabled) {
            this.enabled = enabled;
        }
        
        public boolean isEnabled() {
            return enabled;
        }
        
        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
    }
}

