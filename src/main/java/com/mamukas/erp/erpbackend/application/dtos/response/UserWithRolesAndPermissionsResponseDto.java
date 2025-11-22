package com.mamukas.erp.erpbackend.application.dtos.response;

import com.mamukas.erp.erpbackend.application.dtos.user.UserResponseDto;
import java.util.List;

/**
 * DTO for user response with roles and permissions
 */
public class UserWithRolesAndPermissionsResponseDto {
    
    private UserResponseDto user;
    private List<String> roles; // Array of role names
    private List<String> permissions; // Array of permission names
    
    // Constructors
    public UserWithRolesAndPermissionsResponseDto() {}
    
    public UserWithRolesAndPermissionsResponseDto(UserResponseDto user, List<String> roles, List<String> permissions) {
        this.user = user;
        this.roles = roles;
        this.permissions = permissions;
    }
    
    // Getters and setters
    public UserResponseDto getUser() {
        return user;
    }
    
    public void setUser(UserResponseDto user) {
        this.user = user;
    }
    
    public List<String> getRoles() {
        return roles;
    }
    
    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
    
    public List<String> getPermissions() {
        return permissions;
    }
    
    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
    }
}


