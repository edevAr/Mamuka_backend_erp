package com.mamukas.erp.erpbackend.application.services;

import com.mamukas.erp.erpbackend.domain.entities.User;
import com.mamukas.erp.erpbackend.infrastructure.persistence.jpa.UserJpaEntity;
import com.mamukas.erp.erpbackend.infrastructure.repositories.UserRepository;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorConfig;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import com.warrenstrange.googleauth.GoogleAuthenticatorQRGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service for Two-Factor Authentication (2FA) using TOTP (Time-based One-Time Password)
 * Implements Google Authenticator compatible 2FA
 */
@Service
@Transactional
public class TwoFactorAuthService {
    
    private final UserRepository userRepository;
    private final GoogleAuthenticator googleAuthenticator;
    
    @Value("${app.name:Mamukas ERP}")
    private String appName;
    
    public TwoFactorAuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
        
        // Configure Google Authenticator with 30-second time steps
        GoogleAuthenticatorConfig config = new GoogleAuthenticatorConfig.GoogleAuthenticatorConfigBuilder()
                .setTimeStepSizeInMillis(30000) // 30 seconds
                .setWindowSize(3) // Allow 3 time steps before/after current time
                .build();
        
        this.googleAuthenticator = new GoogleAuthenticator(config);
    }
    
    /**
     * Generate a new 2FA secret and QR code for a user
     * @param userId the user ID
     * @return TwoFactorSetupResponse containing secret and QR code URL
     * @throws IllegalArgumentException if user not found
     */
    public TwoFactorSetupResponse setupTwoFactor(Long userId) {
        UserJpaEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User with ID " + userId + " not found"));
        
        User user = userRepository.toDomain(userEntity);
        
        // Generate new secret
        GoogleAuthenticatorKey key = googleAuthenticator.createCredentials();
        String secret = key.getKey();
        
        // Generate QR code URL
        String qrCodeUrl = GoogleAuthenticatorQRGenerator.getOtpAuthURL(
                appName,
                user.getEmail() != null ? user.getEmail() : user.getUsername(),
                key
        );
        
        // Save secret temporarily (don't enable yet - user needs to verify first)
        user.setTwoFactorSecret(secret);
        user.setTwoFactorEnabled(false); // Not enabled until verified
        userRepository.save(userRepository.toJpaEntity(user));
        
        return new TwoFactorSetupResponse(secret, qrCodeUrl);
    }
    
    /**
     * Verify and enable 2FA for a user
     * @param userId the user ID
     * @param code the 6-digit verification code from authenticator app
     * @return true if code is valid and 2FA is enabled
     * @throws IllegalArgumentException if user not found or code is invalid
     */
    public boolean enableTwoFactor(Long userId, int code) {
        UserJpaEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User with ID " + userId + " not found"));
        
        User user = userRepository.toDomain(userEntity);
        
        if (user.getTwoFactorSecret() == null) {
            throw new IllegalStateException("2FA secret not found. Please setup 2FA first.");
        }
        
        // Verify the code
        boolean isValid = googleAuthenticator.authorize(user.getTwoFactorSecret(), code);
        
        if (!isValid) {
            throw new IllegalArgumentException("Invalid verification code");
        }
        
        // Enable 2FA
        user.setTwoFactorEnabled(true);
        userRepository.save(userRepository.toJpaEntity(user));
        
        return true;
    }
    
    /**
     * Disable 2FA for a user
     * @param userId the user ID
     * @param code the 6-digit verification code from authenticator app (to confirm)
     * @return true if disabled successfully
     * @throws IllegalArgumentException if user not found or code is invalid
     */
    public boolean disableTwoFactor(Long userId, int code) {
        UserJpaEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User with ID " + userId + " not found"));
        
        User user = userRepository.toDomain(userEntity);
        
        if (!Boolean.TRUE.equals(user.getTwoFactorEnabled())) {
            throw new IllegalStateException("2FA is not enabled for this user");
        }
        
        if (user.getTwoFactorSecret() == null) {
            throw new IllegalStateException("2FA secret not found");
        }
        
        // Verify the code before disabling
        boolean isValid = googleAuthenticator.authorize(user.getTwoFactorSecret(), code);
        
        if (!isValid) {
            throw new IllegalArgumentException("Invalid verification code");
        }
        
        // Disable 2FA and clear secret
        user.setTwoFactorEnabled(false);
        user.setTwoFactorSecret(null);
        userRepository.save(userRepository.toJpaEntity(user));
        
        return true;
    }
    
    /**
     * Verify a 2FA code for a user (used during login)
     * @param userId the user ID
     * @param code the 6-digit code from authenticator app
     * @return true if code is valid
     */
    @Transactional(readOnly = true)
    public boolean verifyCode(Long userId, int code) {
        Optional<UserJpaEntity> userEntityOpt = userRepository.findById(userId);
        
        if (userEntityOpt.isEmpty()) {
            return false;
        }
        
        User user = userRepository.toDomain(userEntityOpt.get());
        
        if (!Boolean.TRUE.equals(user.getTwoFactorEnabled()) || user.getTwoFactorSecret() == null) {
            return false;
        }
        
        return googleAuthenticator.authorize(user.getTwoFactorSecret(), code);
    }
    
    /**
     * Check if 2FA is enabled for a user
     * @param userId the user ID
     * @return true if 2FA is enabled
     */
    @Transactional(readOnly = true)
    public boolean isTwoFactorEnabled(Long userId) {
        Optional<UserJpaEntity> userEntityOpt = userRepository.findById(userId);
        
        if (userEntityOpt.isEmpty()) {
            return false;
        }
        
        User user = userRepository.toDomain(userEntityOpt.get());
        return Boolean.TRUE.equals(user.getTwoFactorEnabled());
    }
    
    /**
     * Response DTO for 2FA setup
     */
    public static class TwoFactorSetupResponse {
        private String secret;
        private String qrCodeUrl;
        
        public TwoFactorSetupResponse(String secret, String qrCodeUrl) {
            this.secret = secret;
            this.qrCodeUrl = qrCodeUrl;
        }
        
        public String getSecret() {
            return secret;
        }
        
        public void setSecret(String secret) {
            this.secret = secret;
        }
        
        public String getQrCodeUrl() {
            return qrCodeUrl;
        }
        
        public void setQrCodeUrl(String qrCodeUrl) {
            this.qrCodeUrl = qrCodeUrl;
        }
    }
}

