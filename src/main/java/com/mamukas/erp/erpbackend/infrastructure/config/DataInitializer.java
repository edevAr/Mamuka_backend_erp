package com.mamukas.erp.erpbackend.infrastructure.config;

import com.mamukas.erp.erpbackend.application.services.RoleService;
import com.mamukas.erp.erpbackend.application.services.PermissionService;
import com.mamukas.erp.erpbackend.application.services.RolePermissionService;
import com.mamukas.erp.erpbackend.application.services.ProductService;
import com.mamukas.erp.erpbackend.application.services.StoreService;
import com.mamukas.erp.erpbackend.application.services.WarehouseService;
import com.mamukas.erp.erpbackend.application.services.UserService;
import com.mamukas.erp.erpbackend.application.services.EmployeeStoreService;
import com.mamukas.erp.erpbackend.application.services.EmployeeWarehouseService;
import com.mamukas.erp.erpbackend.application.services.BoxService;
import com.mamukas.erp.erpbackend.application.services.PackService;
import com.mamukas.erp.erpbackend.application.services.WarehouseItemService;
import com.mamukas.erp.erpbackend.infrastructure.repositories.EmployeeStoreRepository;
import com.mamukas.erp.erpbackend.infrastructure.repositories.EmployeeWarehouseRepository;
import com.mamukas.erp.erpbackend.infrastructure.persistence.jpa.EmployeeStoreJpaEntity;
import com.mamukas.erp.erpbackend.application.dtos.user.UserRequestDto;
import com.mamukas.erp.erpbackend.application.dtos.user.UserResponseDto;
import com.mamukas.erp.erpbackend.application.dtos.role.RoleRequestDto;
import com.mamukas.erp.erpbackend.application.dtos.request.PermissionRequestDto;
import com.mamukas.erp.erpbackend.application.dtos.request.RolePermissionRequestDto;
import com.mamukas.erp.erpbackend.application.dtos.request.EmployeeStoreRequestDto;
import com.mamukas.erp.erpbackend.domain.entities.Store;
import com.mamukas.erp.erpbackend.domain.entities.Role;
import com.mamukas.erp.erpbackend.domain.entities.Permission;
import com.mamukas.erp.erpbackend.domain.entities.EmployeeStore;
import com.mamukas.erp.erpbackend.domain.entities.Product;
import com.mamukas.erp.erpbackend.domain.entities.Warehouse;
import com.mamukas.erp.erpbackend.domain.entities.Box;
import com.mamukas.erp.erpbackend.domain.entities.Pack;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * Data initializer that inserts default roles when the application starts
 */
@Component
public class DataInitializer implements ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    @Autowired
    private RoleService roleService;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private RolePermissionService rolePermissionService;

    @Autowired
    private ProductService productService;

    @Autowired
    private StoreService storeService;

    @Autowired
    private WarehouseService warehouseService;

    @Autowired
    private UserService userService;

    @Autowired
    private EmployeeStoreService employeeStoreService;

    @Autowired
    private EmployeeStoreRepository employeeStoreRepository;

    @Autowired
    private BoxService boxService;

    @Autowired
    private PackService packService;

    @Autowired
    private WarehouseItemService warehouseItemService;
    
    @Autowired
    private EmployeeWarehouseService employeeWarehouseService;
    
    @Autowired
    private EmployeeWarehouseRepository employeeWarehouseRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        logger.info("Starting data initialization...");
        initializeDefaultRoles();
        initializeDefaultPermissions();
        initializeCustomerRolePermissions();
        initializeAdminRolePermissions();
        initializeEmployeeRolePermissions();
        initializeDefaultAdminUser();
        initializeTestUsers();
        initializeDefaultProducts();
        initializeDefaultStores();
        initializeDefaultWarehouses();
        initializeDefaultEmployeeStores();
        initializeStoreRates();
        initializeDefaultBoxes();
        initializeDefaultPacks();
        initializeDefaultWarehouseItems();
        initializeDefaultEmployeeWarehouses();
        logger.info("Data initialization completed.");
    }

    /**
     * Initialize default roles in the system
     */
    private void initializeDefaultRoles() {
        try {
            // Define default roles
            String[] defaultRoles = {"Admin", "StorageManager", "WarehouseManager", "Employee", "Customer"};

            for (String roleName : defaultRoles) {
                // Check if role already exists
                if (!roleService.existsByRoleName(roleName)) {
                    RoleRequestDto roleRequest = new RoleRequestDto();
                    roleRequest.setRoleName(roleName);
                    
                    roleService.createRole(roleRequest);
                    logger.info("Created default role: {}", roleName);
                } else {
                    logger.info("Role already exists: {}", roleName);
                }
            }
            
            logger.info("Default roles initialization completed successfully");
            
        } catch (Exception e) {
            logger.error("Error initializing default roles: {}", e.getMessage(), e);
            // Don't stop the application if role initialization fails
        }
    }

    /**
     * Initialize default permissions in the system
     */
    private void initializeDefaultPermissions() {
        try {
            // Define default permissions (authorities que usan los controladores)
            String[] defaultPermissions = {
                // Inventory Management
                "INVENTORY_CREATE",
                "INVENTORY_READ", 
                "INVENTORY_UPDATE",
                "INVENTORY_DELETE",
                "INVENTORY_*",
                
                // User Management
                "USER_MANAGEMENT_CREATE",
                "USER_MANAGEMENT_READ",
                "USER_MANAGEMENT_UPDATE", 
                "USER_MANAGEMENT_DELETE",
                "USER_*",

                // Products Management
                "CREATE_PRODUCTS",
                "READ_PRODUCTS",
                "UPDATE_PRODUCTS",
                "DELETE_PRODUCTS",
                "PRODUCTS_*",

                // Stores Management
                "CREATE_STORES",
                "READ_STORES",
                "UPDATE_STORES",
                "DELETE_STORES",
                "STORES_*",

                // Warehouses Management
                "CREATE_WAREHOUSES",
                "READ_WAREHOUSES",
                "UPDATE_WAREHOUSES",
                "DELETE_WAREHOUSES",
                "WAREHOUSES_*",

                // Sales Management
                "CREATE_SALE",
                "READ_SALE",
                "UPDATE_SALE",
                "DELETE_SALE",
                "SALES_*",


                // Botton bar buttons
                "READ_PRODUCTS_BUTTON", 
                "READ_STORES_BUTTON", 
                "SHOW_NEW_STORES_BUTTON", 
                "SHOW_PROFILE_BUTTON", 
            };

            for (String permissionName : defaultPermissions) {
                // Check if permission already exists
                if (!permissionService.existsByName(permissionName)) {
                    PermissionRequestDto permissionRequest = new PermissionRequestDto();
                    permissionRequest.setName(permissionName);
                    permissionRequest.setStatus(true); // Active by default
                    
                    permissionService.createPermission(permissionRequest);
                    logger.info("Created default permission: {}", permissionName);
                } else {
                    logger.info("Permission already exists: {}", permissionName);
                }
            }
            
            logger.info("Default permissions initialization completed successfully");
            
        } catch (Exception e) {
            logger.error("Error initializing default permissions: {}", e.getMessage(), e);
            // Don't stop the application if permission initialization fails
        }
    }

    /**
     * Initialize Customer role permissions
     */
    private void initializeCustomerRolePermissions() {
        try {
            // Get Customer role
            Role customerRole = roleService.findByRoleName("Customer").orElse(null);
            if (customerRole == null) {
                logger.warn("Customer role not found, skipping role-permission initialization");
                return;
            }

            // Define permissions to assign to Customer role
            String[] customerPermissions = {
                "READ_PRODUCTS",
                "READ_STORES",
                "READ_PRODUCTS_BUTTON", 
                "READ_STORES_BUTTON", 
                "SHOW_NEW_STORES_BUTTON", 
                "SHOW_PROFILE_BUTTON", 
            };

            for (String permissionName : customerPermissions) {
                try {
                    // Get permission
                    Permission permission = permissionService.findByName(permissionName).orElse(null);
                    if (permission == null) {
                        logger.warn("Permission '{}' not found, skipping assignment to Customer role", permissionName);
                        continue;
                    }

                    // Check if assignment already exists
                    if (!rolePermissionService.isPermissionAssignedToRole(customerRole.getIdRole(), permission.getIdPermission())) {
                        // Create role-permission assignment
                        RolePermissionRequestDto requestDto = new RolePermissionRequestDto();
                        requestDto.setIdRole(customerRole.getIdRole());
                        requestDto.setIdPermission(permission.getIdPermission());
                        
                        rolePermissionService.assignPermissionToRole(requestDto);
                        logger.info("Assigned permission '{}' to Customer role", permissionName);
                    } else {
                        logger.info("Permission '{}' already assigned to Customer role", permissionName);
                    }
                } catch (Exception e) {
                    logger.error("Error assigning permission '{}' to Customer role: {}", permissionName, e.getMessage());
                }
            }
            
            logger.info("Customer role permissions initialization completed successfully");
            
        } catch (Exception e) {
            logger.error("Error initializing Customer role permissions: {}", e.getMessage(), e);
            // Don't stop the application if role-permission initialization fails
        }
    }

    /**
     * Initialize Admin role permissions
     */
    private void initializeAdminRolePermissions() {
        try {
            // Get Admin role
            Role adminRole = roleService.findByRoleName("Admin").orElse(null);
            if (adminRole == null) {
                logger.warn("Admin role not found, skipping admin role-permission initialization");
                return;
            }

            // Define permissions to assign to Admin role (all permissions)
            String[] adminPermissions = {
                "INVENTORY_CREATE",
                "INVENTORY_READ", 
                "INVENTORY_UPDATE",
                "INVENTORY_DELETE",
                "USER_MANAGEMENT_CREATE",
                "USER_MANAGEMENT_READ",
                "USER_MANAGEMENT_UPDATE", 
                "USER_MANAGEMENT_DELETE",
                "READ_PRODUCTS_BUTTON", 
                "READ_STORES_BUTTON", 
                "SHOW_NEW_STORES_BUTTON", 
                "SHOW_PROFILE_BUTTON",
                "INVENTORY_*",
                "USER_*",
                "PRODUCTS_*",
                "STORES_*",
                "WAREHOUSES_*",
                "SALES_*"
            };

            for (String permissionName : adminPermissions) {
                try {
                    // Get permission
                    Permission permission = permissionService.findByName(permissionName).orElse(null);
                    if (permission == null) {
                        logger.warn("Permission '{}' not found, skipping assignment to Admin role", permissionName);
                        continue;
                    }

                    // Check if assignment already exists
                    if (!rolePermissionService.isPermissionAssignedToRole(adminRole.getIdRole(), permission.getIdPermission())) {
                        // Create role-permission assignment
                        RolePermissionRequestDto requestDto = new RolePermissionRequestDto();
                        requestDto.setIdRole(adminRole.getIdRole());
                        requestDto.setIdPermission(permission.getIdPermission());
                        
                        rolePermissionService.assignPermissionToRole(requestDto);
                        logger.info("Assigned permission '{}' to Admin role", permissionName);
                    } else {
                        logger.info("Permission '{}' already assigned to Admin role", permissionName);
                    }
                } catch (Exception e) {
                    logger.error("Error assigning permission '{}' to Admin role: {}", permissionName, e.getMessage());
                }
            }
            
            logger.info("Admin role permissions initialization completed successfully");
            
        } catch (Exception e) {
            logger.error("Error initializing Admin role permissions: {}", e.getMessage(), e);
            // Don't stop the application if role-permission initialization fails
        }
    }

    /**
     * Initialize Employee role permissions
     */
    private void initializeEmployeeRolePermissions() {
        try {
            // Get Employee role
            Role employeeRole = roleService.findByRoleName("Employee").orElse(null);
            if (employeeRole == null) {
                logger.warn("Employee role not found, skipping employee role-permission initialization");
                return;
            }

            // Define permissions to assign to Employee role
            String[] employeePermissions = {
                "PRODUCTS_*",
                "STORES_*",
                "WAREHOUSES_*",
                "SALES_*"
            };

            for (String permissionName : employeePermissions) {
                try {
                    // Get permission
                    Permission permission = permissionService.findByName(permissionName).orElse(null);
                    if (permission == null) {
                        logger.warn("Permission '{}' not found, skipping assignment to Employee role", permissionName);
                        continue;
                    }

                    // Check if assignment already exists
                    if (!rolePermissionService.isPermissionAssignedToRole(employeeRole.getIdRole(), permission.getIdPermission())) {
                        // Create role-permission assignment
                        RolePermissionRequestDto requestDto = new RolePermissionRequestDto();
                        requestDto.setIdRole(employeeRole.getIdRole());
                        requestDto.setIdPermission(permission.getIdPermission());
                        
                        rolePermissionService.assignPermissionToRole(requestDto);
                        logger.info("Assigned permission '{}' to Employee role", permissionName);
                    } else {
                        logger.info("Permission '{}' already assigned to Employee role", permissionName);
                    }
                } catch (Exception e) {
                    logger.error("Error assigning permission '{}' to Employee role: {}", permissionName, e.getMessage());
                }
            }
            
            logger.info("Employee role permissions initialization completed successfully");
            
        } catch (Exception e) {
            logger.error("Error initializing Employee role permissions: {}", e.getMessage(), e);
            // Don't stop the application if role-permission initialization fails
        }
    }

    /**
     * Initialize default admin user
     */
    private void initializeDefaultAdminUser() {
        try {
            logger.info("Initializing default admin user...");

            // Check if admin user already exists
            String adminUsername = "admin";
            String adminEmail = "admin@mamukas.com";
            
            if (userService.existsByUsername(adminUsername) || userService.existsByEmail(adminEmail)) {
                logger.info("Admin user already exists, skipping creation");
                return;
            }

            // Create admin user
            UserRequestDto adminUser = new UserRequestDto();
            adminUser.setUsername(adminUsername);
            adminUser.setEmail(adminEmail);
            adminUser.setPassword("admin123"); // Default password - should be changed in production
            adminUser.setName("Administrator");
            adminUser.setLastName("System");
            adminUser.setAge(30);
            adminUser.setCi("00000000");
            adminUser.setStatus(1); // Active
            adminUser.setRoleName("Admin");

            userService.createUser(adminUser);
            logger.info("Default admin user created successfully - Username: '{}', Password: 'admin123'", adminUsername);
            logger.warn("SECURITY WARNING: Default admin user created with password 'admin123'. Please change it in production!");

        } catch (Exception e) {
            logger.error("Error creating default admin user: {}", e.getMessage(), e);
            // Don't stop the application if admin user creation fails
        }
    }

    /**
     * Initialize 25 default products with test data
     */
    private void initializeDefaultProducts() {
        try {
            logger.info("Initializing default products...");

            // Array of product data: {name, status, price, expirationDate}
            Object[][] products = {
                {"Laptop HP Pavilion", "Active", new BigDecimal("899.99"), LocalDate.of(2025, 12, 31)},
                {"Mouse Logitech MX Master", "Active", new BigDecimal("99.99"), LocalDate.of(2026, 6, 30)},
                {"Teclado Mecánico RGB", "Active", new BigDecimal("149.99"), LocalDate.of(2026, 12, 31)},
                {"Monitor Samsung 24\"", "Active", new BigDecimal("199.99"), LocalDate.of(2027, 3, 31)},
                {"Auriculares Sony WH-1000XM4", "Active", new BigDecimal("299.99"), LocalDate.of(2026, 9, 30)},
                {"Smartphone iPhone 14", "Active", new BigDecimal("799.99"), LocalDate.of(2025, 12, 31)},
                {"Tablet iPad Air", "Active", new BigDecimal("599.99"), LocalDate.of(2026, 6, 30)},
                {"Cámara Canon EOS R6", "Active", new BigDecimal("2499.99"), LocalDate.of(2027, 12, 31)},
                {"Impresora HP LaserJet", "Active", new BigDecimal("349.99"), LocalDate.of(2028, 3, 31)},
                {"Disco Duro Externo 2TB", "Active", new BigDecimal("89.99"), LocalDate.of(2026, 12, 31)},
                {"Webcam Logitech C920", "Active", new BigDecimal("79.99"), LocalDate.of(2026, 9, 30)},
                {"Router Wi-Fi 6", "Active", new BigDecimal("159.99"), LocalDate.of(2027, 6, 30)},
                {"Altavoces Bluetooth JBL", "Active", new BigDecimal("179.99"), LocalDate.of(2026, 12, 31)},
                {"SSD Samsung 1TB", "Active", new BigDecimal("129.99"), LocalDate.of(2028, 6, 30)},
                {"Tarjeta Gráfica RTX 4060", "Active", new BigDecimal("399.99"), LocalDate.of(2026, 3, 31)},
                {"Procesador AMD Ryzen 7", "Active", new BigDecimal("299.99"), LocalDate.of(2027, 12, 31)},
                {"Memoria RAM 32GB", "Active", new BigDecimal("189.99"), LocalDate.of(2028, 6, 30)},
                {"Fuente de Poder 750W", "Active", new BigDecimal("119.99"), LocalDate.of(2027, 9, 30)},
                {"Case Gaming RGB", "Active", new BigDecimal("89.99"), LocalDate.of(2026, 12, 31)},
                {"Cooler CPU Líquido", "Active", new BigDecimal("149.99"), LocalDate.of(2027, 3, 31)},
                {"Switch Ethernet 24 puertos", "Active", new BigDecimal("299.99"), LocalDate.of(2028, 12, 31)},
                {"UPS 1500VA", "Active", new BigDecimal("199.99"), LocalDate.of(2027, 6, 30)},
                {"Servidor Rack 1U", "Active", new BigDecimal("1999.99"), LocalDate.of(2029, 12, 31)},
                {"Cable HDMI 4K", "Active", new BigDecimal("24.99"), LocalDate.of(2026, 6, 30)},
                {"Hub USB-C 7 en 1", "Active", new BigDecimal("49.99"), LocalDate.of(2026, 9, 30)}
            };

            // Create products
            for (Object[] productData : products) {
                try {
                    String name = (String) productData[0];
                    String status = (String) productData[1];
                    BigDecimal price = (BigDecimal) productData[2];
                    LocalDate expirationDate = (LocalDate) productData[3];
                    
                    // Stock is now read directly from boxes or packs tables
                    // So we don't need to set it here
                    String descripcion = "Producto: " + name;

                    productService.createProduct(name, status, price, expirationDate, descripcion);
                    logger.info("Created product: {}", name);
                } catch (Exception e) {
                    logger.error("Error creating product '{}': {}", productData[0], e.getMessage());
                }
            }

            logger.info("Default products initialization completed successfully");

        } catch (Exception e) {
            logger.error("Error initializing default products: {}", e.getMessage(), e);
            // Don't stop the application if product initialization fails
        }
    }

    /**
     * Initialize 8 default stores with test data
     */
    private void initializeDefaultStores() {
        try {
            logger.info("Initializing default stores...");

            // Array of store data: {name, address, businessHours, idCompany}
            Object[][] stores = {
                {"TechStore Centro", "Av. Principal 123, Centro Histórico", "Lunes a Viernes: 8:00 AM - 8:00 PM, Sábados: 9:00 AM - 6:00 PM", 1L},
                {"TechStore Norte", "Blvd. Norte 456, Zona Residencial Norte", "Lunes a Sábado: 9:00 AM - 9:00 PM, Domingos: 10:00 AM - 6:00 PM", 1L},
                {"TechStore Sur", "Av. Sur 789, Plaza Comercial del Sur", "Lunes a Domingo: 10:00 AM - 10:00 PM", 1L},
                {"TechStore Este", "Calle Este 321, Centro Comercial Oriente", "Lunes a Viernes: 9:00 AM - 9:00 PM, Fines de semana: 10:00 AM - 8:00 PM", 1L},
                {"TechStore Oeste", "Av. Poniente 654, Mall del Oeste", "Todos los días: 10:00 AM - 9:00 PM", 1L},
                {"TechStore Express", "Aeropuerto Internacional, Terminal A", "24 horas, todos los días", 1L},
                {"TechStore Universitaria", "Av. Universidad 987, Ciudad Universitaria", "Lunes a Viernes: 8:00 AM - 7:00 PM, Sábados: 9:00 AM - 5:00 PM", 1L},
                {"TechStore Outlet", "Zona Industrial 147, Parque Industrial", "Lunes a Sábado: 9:00 AM - 6:00 PM", 1L}
            };

            // Create stores
            for (Object[] storeData : stores) {
                try {
                    String name = (String) storeData[0];
                    String address = (String) storeData[1];
                    String businessHours = (String) storeData[2];
                    Long idCompany = (Long) storeData[3];

                    storeService.createStore(name, address, businessHours, idCompany);
                    logger.info("Created store: {}", name);
                } catch (Exception e) {
                    logger.error("Error creating store '{}': {}", storeData[0], e.getMessage());
                }
            }

            logger.info("Default stores initialization completed successfully");

        } catch (Exception e) {
            logger.error("Error initializing default stores: {}", e.getMessage(), e);
            // Don't stop the application if store initialization fails
        }
    }

    /**
     * Initialize 10 default warehouses with test data
     */
    private void initializeDefaultWarehouses() {
        try {
            logger.info("Initializing default warehouses...");

            // Array of warehouse data: {name, address}
            Object[][] warehouses = {
                {"Almacén Central Norte", "Parque Industrial Norte, Nave A-1, Km 15 Carretera Industrial"},
                {"Bodega Logística Sur", "Zona Logística Sur, Bodega Central B-23, Av. Distribución 456"},
                {"Complejo Logístico Este", "Complejo Logístico Este, Módulo C-7, Blvd. Comercial 789"},
                {"Centro Distribución Oeste", "Centro de Distribución Oeste, Almacén D-12, Calle Logística 321"},
                {"Hub Metropolitano", "Hub Metropolitano, Facility E-5, Anillo Periférico Norte 654"},
                {"Terminal Carga Aérea", "Terminal de Carga Aérea, Hangar F-18, Zona Aeroportuaria 987"},
                {"Puerto Seco Intermodal", "Puerto Seco Intermodal, Patio G-9, Vía Férrea Industrial 147"},
                {"Almacén Refrigerado", "Centro Logístico Refrigerado, Cámara H-4, Zona Fría 258"},
                {"Almacén Productos Peligrosos", "Almacén de Productos Peligrosos, Instalación I-11, Área Especial 369"},
                {"Depósito Alta Rotación", "Depósito de Alta Rotación, Plataforma J-6, Corredor Express 159"}
            };

            // Create warehouses
            for (Object[] warehouseData : warehouses) {
                try {
                    String name = (String) warehouseData[0];
                    String address = (String) warehouseData[1];

                    warehouseService.createWarehouse(name, address);
                    logger.info("Created warehouse: {} at {}", name, address.substring(0, Math.min(50, address.length())) + "...");
                } catch (Exception e) {
                    logger.error("Error creating warehouse '{}': {}", warehouseData[0], e.getMessage());
                }
            }

            logger.info("Default warehouses initialization completed successfully");

        } catch (Exception e) {
            logger.error("Error initializing default warehouses: {}", e.getMessage(), e);
            // Don't stop the application if warehouse initialization fails
        }
    }

    /**
     * Initialize 10 default employee-store assignments with test data
     */
    private void initializeDefaultEmployeeStores() {
        try {
            logger.info("=== STARTING employee-store assignments initialization ===");

            // Get all users and stores
            java.util.List<UserResponseDto> users = userService.getAllUsers();
            java.util.List<Store> stores = storeService.findAll();

            logger.info("Found {} users and {} stores", users.size(), stores.size());

            if (users.isEmpty()) {
                logger.error("CRITICAL: No users found! Cannot create employee-store assignments.");
                return;
            }

            if (stores.isEmpty()) {
                logger.error("CRITICAL: No stores found! Cannot create employee-store assignments.");
                return;
            }

            // Log user and store IDs for debugging
            logger.info("User IDs: {}", users.stream().map(u -> u.getIdUser()).collect(java.util.stream.Collectors.toList()));
            logger.info("Store IDs: {}", stores.stream().map(s -> s.getIdStore()).collect(java.util.stream.Collectors.toList()));

            // Create exactly 10 employee-store assignments with random selection
            int targetCount = 10;
            int createdCount = 0;
            int skippedCount = 0;
            int errorCount = 0;
            int attempts = 0;
            int maxAttempts = users.size() * stores.size() * 2; // Allow more attempts for random selection
            int consecutiveErrors = 0;
            final int MAX_CONSECUTIVE_ERRORS = 3; // Stop after 3 consecutive errors
            Random random = new Random();

            logger.info("Target: Create {} employee-store assignments (random selection, max attempts: {})", targetCount, maxAttempts);

            // Try to create assignments with random user-store combinations
            while (createdCount < targetCount && attempts < maxAttempts) {
                attempts++;
                
                // Select random user and store
                UserResponseDto user = users.get(random.nextInt(users.size()));
                Store store = stores.get(random.nextInt(stores.size()));
                
                Long idEmployee = user.getIdUser();
                Long idStore = store.getIdStore();

                    logger.info("Attempt {}: Trying User ID {} -> Store ID {}", attempts, idEmployee, idStore);

                    if (idEmployee == null) {
                        logger.warn("  -> User has null ID, skipping. User: {}", user.getUsername());
                        skippedCount++;
                        continue;
                    }

                    if (idStore == null) {
                        logger.warn("  -> Store has null ID, skipping. Store: {}", store.getName());
                        skippedCount++;
                        continue;
                    }

                    // Check if assignment already exists
                    boolean exists = employeeStoreRepository.existsByIdEmployeeAndIdStore(idEmployee, idStore);
                    logger.info("  -> Assignment exists? {}", exists);
                    
                    if (!exists) {
                        try {
                            // Use the service method which has all validations
                            EmployeeStoreRequestDto request = new EmployeeStoreRequestDto();
                            request.setIdEmployee(idEmployee);
                            request.setIdStore(idStore);
                            
                            logger.info("  -> Calling assignEmployeeToStore...");
                            employeeStoreService.assignEmployeeToStore(request);
                            
                            // Verify it was created
                            boolean nowExists = employeeStoreRepository.existsByIdEmployeeAndIdStore(idEmployee, idStore);
                            if (nowExists) {
                                logger.info("  -> SUCCESS! Created assignment: User ID {} -> Store ID {}", idEmployee, idStore);
                                createdCount++;
                                consecutiveErrors = 0; // Reset error counter on success
                            } else {
                                logger.error("  -> FAILED! Assignment was not created even though no error was thrown!");
                                errorCount++;
                                consecutiveErrors++;
                            }
                        } catch (Exception e) {
                            logger.error("  -> ERROR saving assignment (User ID {} -> Store ID {}): {}", 
                                idEmployee, idStore, e.getMessage());
                            errorCount++;
                            consecutiveErrors++;
                            
                            // Stop if we have too many consecutive errors (likely a persistent issue)
                            if (consecutiveErrors >= MAX_CONSECUTIVE_ERRORS) {
                                logger.error("  -> STOPPING: {} consecutive errors detected. Likely a database schema issue.", consecutiveErrors);
                                logger.error("  -> Please execute: ALTER TABLE employee_stores DROP COLUMN IF EXISTS id_employee;");
                                break;
                            }
                        }
                    } else {
                        logger.info("  -> Skipped: Assignment already exists");
                        skippedCount++;
                    }
            }

            logger.info("=== employee-store assignments initialization COMPLETED ===");
            logger.info("Results: Created={}, Skipped={}, Errors={}, Total Attempts={}", 
                createdCount, skippedCount, errorCount, attempts);

            // Verify the records were actually created in the database
            long totalRecords = employeeStoreRepository.count();
            logger.info("VERIFICATION: Total employee-store records in database: {}", totalRecords);
            
            if (totalRecords == 0 && createdCount > 0) {
                logger.error("WARNING: Created {} records but database shows 0 records! Transaction may not have committed.", createdCount);
            }

        } catch (Exception e) {
            logger.error("CRITICAL ERROR initializing default employee-store assignments: {}", e.getMessage(), e);
            e.printStackTrace();
            // Don't stop the application if employee-store initialization fails
        }
    }

    /**
     * Initialize random rates (1.0 to 5.0) for all existing stores
     */
    private void initializeStoreRates() {
        try {
            logger.info("Initializing store rates...");

            // Get all stores
            java.util.List<Store> stores = storeService.findAll();

            if (stores.isEmpty()) {
                logger.warn("No stores found, skipping rate initialization");
                return;
            }

            Random random = new Random();
            int updatedCount = 0;

            for (Store store : stores) {
                try {
                    // Generate random rate between 1.0 and 5.0
                    // Format: 1 decimal place (e.g., 3.5, 4.2, 2.8)
                    double rate = Math.round((1.0 + random.nextDouble() * 4.0) * 10.0) / 10.0;
                    
                    // Ensure rate is between 1.0 and 5.0
                    if (rate < 1.0) rate = 1.0;
                    if (rate > 5.0) rate = 5.0;
                    
                    store.setRate(rate);
                    storeService.save(store);
                    
                    logger.info("Assigned rate {} to store: {}", rate, store.getName());
                    updatedCount++;
                } catch (Exception e) {
                    logger.error("Error assigning rate to store '{}': {}", store.getName(), e.getMessage());
                }
            }

            logger.info("Store rates initialization completed successfully. Updated {} stores.", updatedCount);

        } catch (Exception e) {
            logger.error("Error initializing store rates: {}", e.getMessage(), e);
            // Don't stop the application if rate initialization fails
        }
    }

    /**
     * Initialize 20 default boxes with random product IDs
     */
    private void initializeDefaultBoxes() {
        try {
            logger.info("Initializing default boxes...");

            // Get all products
            java.util.List<Product> products = productService.findAll();

            if (products.isEmpty()) {
                logger.warn("No products found, skipping box initialization");
                return;
            }

            Random random = new Random();
            int createdCount = 0;
            int skippedCount = 0;
            int maxAttempts = products.size() * 2; // Allow enough attempts to find unique products
            int attempts = 0;
            java.util.Set<Long> usedProductIds = new java.util.HashSet<>(); // Track used product IDs

            // Generate boxes ensuring each product has only one box
            while (createdCount < 20 && attempts < maxAttempts) {
                attempts++;
                try {
                    // Select random product that hasn't been used yet
                    Product randomProduct = products.get(random.nextInt(products.size()));
                    Long idProduct = randomProduct.getIdProduct();

                    // Check if this product already has a box
                    if (usedProductIds.contains(idProduct)) {
                        skippedCount++;
                        continue; // Skip if product already has a box
                    }

                    // Check if box already exists in database
                    java.util.List<com.mamukas.erp.erpbackend.domain.entities.Box> existingBoxes = boxService.findByIdProduct(idProduct);
                    if (!existingBoxes.isEmpty()) {
                        logger.info("Box already exists for product ID {}, skipping", idProduct);
                        usedProductIds.add(idProduct);
                        skippedCount++;
                        continue;
                    }

                    // Check if product already exists in packs table
                    java.util.List<com.mamukas.erp.erpbackend.domain.entities.Pack> existingPacks = packService.findByIdProduct(idProduct);
                    if (!existingPacks.isEmpty()) {
                        logger.info("Product ID {} already exists in packs table, skipping box creation", idProduct);
                        usedProductIds.add(idProduct);
                        skippedCount++;
                        continue;
                    }

                    // Generate box name
                    String boxName = "Caja " + (createdCount + 1) + " - " + randomProduct.getName();

                    // Generate random expiration date (between 30 and 365 days from now)
                    int daysToAdd = 30 + random.nextInt(335);
                    LocalDate expirationDate = LocalDate.now().plusDays(daysToAdd);

                    // Generate random units (between 10 and 100)
                    Integer units = 10 + random.nextInt(91);

                    // Create box
                    com.mamukas.erp.erpbackend.domain.entities.Box box = boxService.createBox(idProduct, boxName, expirationDate, units);
                    
                    // Generate random units_box (between 1 and 20)
                    Integer unitsBox = 1 + random.nextInt(20);
                    box.setUnitsBox(unitsBox);
                    
                    // Calculate stock = units * units_box
                    Integer stock = (units != null && unitsBox != null) ? units * unitsBox : 0;
                    box.setStock(stock);
                    
                    boxService.save(box);
                    
                    usedProductIds.add(idProduct); // Mark this product as used
                    logger.info("Created box {}: {} (Product ID: {}, Units: {}, UnitsBox: {}, Stock: {})", createdCount + 1, boxName, idProduct, units, unitsBox, stock);
                    createdCount++;
                } catch (Exception e) {
                    logger.error("Error creating box: {}", e.getMessage());
                }
            }

            if (createdCount < 20) {
                logger.warn("Could only create {} boxes out of 20 requested. Skipped {} duplicates. Total attempts: {}", createdCount, skippedCount, attempts);
            }

            logger.info("Default boxes initialization completed successfully. Created {} boxes.", createdCount);

        } catch (Exception e) {
            logger.error("Error initializing default boxes: {}", e.getMessage(), e);
            // Don't stop the application if box initialization fails
        }
    }

    /**
     * Initialize 20 default packs with random product IDs
     */
    private void initializeDefaultPacks() {
        try {
            logger.info("Initializing default packs...");

            // Get all products
            java.util.List<Product> products = productService.findAll();

            if (products.isEmpty()) {
                logger.warn("No products found, skipping pack initialization");
                return;
            }

            Random random = new Random();
            int createdCount = 0;
            int skippedCount = 0;
            int maxAttempts = products.size() * 2; // Allow enough attempts to find unique products
            int attempts = 0;
            java.util.Set<Long> usedProductIds = new java.util.HashSet<>(); // Track used product IDs

            // Generate packs ensuring each product has only one pack
            while (createdCount < 20 && attempts < maxAttempts) {
                attempts++;
                try {
                    // Select random product that hasn't been used yet
                    Product randomProduct = products.get(random.nextInt(products.size()));
                    Long idProduct = randomProduct.getIdProduct();

                    // Check if this product already has a pack
                    if (usedProductIds.contains(idProduct)) {
                        skippedCount++;
                        continue; // Skip if product already has a pack
                    }

                    // Check if pack already exists in database
                    java.util.List<com.mamukas.erp.erpbackend.domain.entities.Pack> existingPacks = packService.findByIdProduct(idProduct);
                    if (!existingPacks.isEmpty()) {
                        logger.info("Pack already exists for product ID {}, skipping", idProduct);
                        usedProductIds.add(idProduct);
                        skippedCount++;
                        continue;
                    }

                    // Check if product already exists in boxes table
                    java.util.List<com.mamukas.erp.erpbackend.domain.entities.Box> existingBoxes = boxService.findByIdProduct(idProduct);
                    if (!existingBoxes.isEmpty()) {
                        logger.info("Product ID {} already exists in boxes table, skipping pack creation", idProduct);
                        usedProductIds.add(idProduct);
                        skippedCount++;
                        continue;
                    }

                    // Generate pack name
                    String packName = "Paquete " + (createdCount + 1) + " - " + randomProduct.getName();

                    // Generate random expiration date (between 30 and 365 days from now)
                    int daysToAdd = 30 + random.nextInt(335);
                    LocalDate expirationDate = LocalDate.now().plusDays(daysToAdd);

                    // Generate random units (between 5 and 50)
                    Integer units = 5 + random.nextInt(46);

                    // Create pack
                    com.mamukas.erp.erpbackend.domain.entities.Pack pack = packService.createPack(idProduct, packName, expirationDate, units);
                    
                    // Generate random units_pack (between 1 and 15)
                    Integer unitsPack = 1 + random.nextInt(15);
                    pack.setUnitsPack(unitsPack);
                    
                    // Calculate stock = units * units_pack
                    Integer stock = (units != null && unitsPack != null) ? units * unitsPack : 0;
                    pack.setStock(stock);
                    
                    packService.save(pack);
                    
                    usedProductIds.add(idProduct); // Mark this product as used
                    logger.info("Created pack {}: {} (Product ID: {}, Units: {}, UnitsPack: {}, Stock: {})", createdCount + 1, packName, idProduct, units, unitsPack, stock);
                    createdCount++;
                } catch (Exception e) {
                    logger.error("Error creating pack: {}", e.getMessage());
                }
            }

            if (createdCount < 20) {
                logger.warn("Could only create {} packs out of 20 requested. Skipped {} duplicates. Total attempts: {}", createdCount, skippedCount, attempts);
            }

            logger.info("Default packs initialization completed successfully. Created {} packs.", createdCount);

        } catch (Exception e) {
            logger.error("Error initializing default packs: {}", e.getMessage(), e);
            // Don't stop the application if pack initialization fails
        }
    }

    /**
     * Initialize 20 default employee-warehouse assignments with test data
     */
    private void initializeDefaultEmployeeWarehouses() {
        try {
            logger.info("=== STARTING employee-warehouse assignments initialization ===");

            // Get all users and warehouses
            java.util.List<UserResponseDto> users = userService.getAllUsers();
            java.util.List<Warehouse> warehouses = warehouseService.findAll();

            logger.info("Found {} users and {} warehouses", users.size(), warehouses.size());

            if (users.isEmpty()) {
                logger.error("CRITICAL: No users found! Cannot create employee-warehouse assignments.");
                return;
            }

            if (warehouses.isEmpty()) {
                logger.error("CRITICAL: No warehouses found! Cannot create employee-warehouse assignments.");
                return;
            }

            // Log user and warehouse IDs for debugging
            logger.info("User IDs: {}", users.stream().map(u -> u.getIdUser()).collect(java.util.stream.Collectors.toList()));
            logger.info("Warehouse IDs: {}", warehouses.stream().map(w -> w.getIdWarehouse()).collect(java.util.stream.Collectors.toList()));

            // Create exactly 20 employee-warehouse assignments
            // First, guarantee at least 3 assignments for user ID 10
            int targetCount = 20;
            int createdCount = 0;
            int skippedCount = 0;
            int errorCount = 0;
            int attempts = 0;
            int maxAttempts = users.size() * warehouses.size() * 2; // Allow more attempts for random selection
            int consecutiveErrors = 0;
            final int MAX_CONSECUTIVE_ERRORS = 3; // Stop after 3 consecutive errors
            Random random = new Random();
            java.util.Set<String> usedCombinations = new java.util.HashSet<>(); // Track used combinations

            logger.info("Target: Create {} employee-warehouse assignments (max attempts: {})", targetCount, maxAttempts);

            // STEP 1: Guarantee at least 3 assignments for user ID 10
            Long targetUserId = 10L;
            int guaranteedForUser10 = 0;
            int targetGuaranteed = 3;
            
            // Check if user 10 exists
            boolean user10Exists = users.stream().anyMatch(u -> u.getIdUser() != null && u.getIdUser().equals(targetUserId));
            
            if (user10Exists) {
                logger.info("=== STEP 1: Creating {} guaranteed assignments for User ID {} ===", targetGuaranteed, targetUserId);
                
                // Shuffle warehouses to get random selection
                java.util.List<Warehouse> shuffledWarehouses = new java.util.ArrayList<>(warehouses);
                java.util.Collections.shuffle(shuffledWarehouses, random);
                
                for (Warehouse warehouse : shuffledWarehouses) {
                    if (guaranteedForUser10 >= targetGuaranteed) {
                        break;
                    }
                    
                    Long idWarehouse = warehouse.getIdWarehouse();
                    if (idWarehouse == null) {
                        continue;
                    }
                    
                    String combinationKey = targetUserId + "-" + idWarehouse;
                    
                    // Check if assignment already exists
                    boolean exists = employeeWarehouseRepository.findByIdUserAndIdWarehouse(targetUserId, idWarehouse).isPresent();
                    
                    if (!exists && !usedCombinations.contains(combinationKey)) {
                        try {
                            employeeWarehouseService.assignEmployeeToWarehouse(targetUserId, idWarehouse);
                            
                            // Verify it was created
                            boolean nowExists = employeeWarehouseRepository.findByIdUserAndIdWarehouse(targetUserId, idWarehouse).isPresent();
                            if (nowExists) {
                                logger.info("  -> SUCCESS! Created guaranteed assignment: User ID {} -> Warehouse ID {}", targetUserId, idWarehouse);
                                usedCombinations.add(combinationKey);
                                createdCount++;
                                guaranteedForUser10++;
                                consecutiveErrors = 0;
                            } else {
                                logger.error("  -> FAILED! Assignment was not created even though no error was thrown!");
                                errorCount++;
                                consecutiveErrors++;
                            }
                        } catch (Exception e) {
                            logger.error("  -> ERROR saving guaranteed assignment (User ID {} -> Warehouse ID {}): {}", 
                                targetUserId, idWarehouse, e.getMessage());
                            errorCount++;
                            consecutiveErrors++;
                            
                            if (consecutiveErrors >= MAX_CONSECUTIVE_ERRORS) {
                                logger.error("  -> STOPPING: {} consecutive errors detected.", consecutiveErrors);
                                break;
                            }
                        }
                    } else {
                        logger.info("  -> Skipped: Assignment User ID {} -> Warehouse ID {} already exists", targetUserId, idWarehouse);
                        usedCombinations.add(combinationKey);
                        skippedCount++;
                    }
                }
                
                logger.info("=== STEP 1 COMPLETED: Created {} guaranteed assignments for User ID {} ===", guaranteedForUser10, targetUserId);
            } else {
                logger.warn("User ID {} not found, skipping guaranteed assignments", targetUserId);
            }

            // STEP 2: Complete the rest with random assignments (up to 20 total)
            logger.info("=== STEP 2: Creating remaining assignments (random selection) ===");
            
            while (createdCount < targetCount && attempts < maxAttempts) {
                attempts++;
                
                // Select random user and warehouse
                UserResponseDto user = users.get(random.nextInt(users.size()));
                Warehouse warehouse = warehouses.get(random.nextInt(warehouses.size()));
                
                Long idUser = user.getIdUser();
                Long idWarehouse = warehouse.getIdWarehouse();

                logger.info("Attempt {}: Trying User ID {} -> Warehouse ID {}", attempts, idUser, idWarehouse);

                if (idUser == null) {
                    logger.warn("  -> User has null ID, skipping. User: {}", user.getUsername());
                    skippedCount++;
                    continue;
                }

                if (idWarehouse == null) {
                    logger.warn("  -> Warehouse has null ID, skipping. Warehouse: {}", warehouse.getName());
                    skippedCount++;
                    continue;
                }

                // Create unique key for combination
                String combinationKey = idUser + "-" + idWarehouse;
                if (usedCombinations.contains(combinationKey)) {
                    logger.info("  -> Skipped: Combination already used in this session");
                    skippedCount++;
                    continue;
                }

                // Check if assignment already exists in database
                boolean exists = employeeWarehouseRepository.findByIdUserAndIdWarehouse(idUser, idWarehouse).isPresent();
                logger.info("  -> Assignment exists? {}", exists);
                
                if (!exists) {
                    try {
                        // Use the service method which has all validations
                        employeeWarehouseService.assignEmployeeToWarehouse(idUser, idWarehouse);
                        
                        // Verify it was created
                        boolean nowExists = employeeWarehouseRepository.findByIdUserAndIdWarehouse(idUser, idWarehouse).isPresent();
                        if (nowExists) {
                            logger.info("  -> SUCCESS! Created assignment: User ID {} -> Warehouse ID {}", idUser, idWarehouse);
                            usedCombinations.add(combinationKey);
                            createdCount++;
                            consecutiveErrors = 0; // Reset error counter on success
                        } else {
                            logger.error("  -> FAILED! Assignment was not created even though no error was thrown!");
                            errorCount++;
                            consecutiveErrors++;
                        }
                    } catch (Exception e) {
                        logger.error("  -> ERROR saving assignment (User ID {} -> Warehouse ID {}): {}", 
                            idUser, idWarehouse, e.getMessage());
                        errorCount++;
                        consecutiveErrors++;
                        
                        // Stop if we have too many consecutive errors (likely a persistent issue)
                        if (consecutiveErrors >= MAX_CONSECUTIVE_ERRORS) {
                            logger.error("  -> STOPPING: {} consecutive errors detected.", consecutiveErrors);
                            break;
                        }
                    }
                } else {
                    logger.info("  -> Skipped: Assignment already exists");
                    usedCombinations.add(combinationKey);
                    skippedCount++;
                }
            }

            logger.info("=== employee-warehouse assignments initialization COMPLETED ===");
            logger.info("Results: Created={}, Skipped={}, Errors={}, Total Attempts={}", 
                createdCount, skippedCount, errorCount, attempts);

            // Verify the records were actually created in the database
            long totalRecords = employeeWarehouseRepository.count();
            logger.info("VERIFICATION: Total employee-warehouse records in database: {}", totalRecords);
            
            if (totalRecords == 0 && createdCount > 0) {
                logger.error("WARNING: Created {} records but database shows 0 records! Transaction may not have committed.", createdCount);
            }

        } catch (Exception e) {
            logger.error("CRITICAL ERROR initializing default employee-warehouse assignments: {}", e.getMessage(), e);
            e.printStackTrace();
            // Don't stop the application if employee-warehouse initialization fails
        }
    }

    /**
     * Initialize 20 default warehouse-items relationships with random assignments
     */
    private void initializeDefaultWarehouseItems() {
        try {
            logger.info("Initializing default warehouse-items relationships...");

            // Get all warehouses, boxes, and packs
            java.util.List<Warehouse> warehouses = warehouseService.findAll();
            java.util.List<Box> boxes = boxService.findAll();
            java.util.List<Pack> packs = packService.findAll();

            if (warehouses.isEmpty()) {
                logger.warn("No warehouses found, skipping warehouse-items initialization");
                return;
            }

            if (boxes.isEmpty() && packs.isEmpty()) {
                logger.warn("No boxes or packs found, skipping warehouse-items initialization");
                return;
            }

            Random random = new Random();
            int createdCount = 0;
            int skippedCount = 0;
            int errorCount = 0;
            int maxAttempts = 100; // Maximum attempts to create 20 unique relationships
            int attempts = 0;

            logger.info("Found {} warehouses, {} boxes, and {} packs", warehouses.size(), boxes.size(), packs.size());

            // Generate 20 warehouse-item relationships
            while (createdCount < 20 && attempts < maxAttempts) {
                attempts++;
                
                try {
                    // Select random warehouse
                    Warehouse randomWarehouse = warehouses.get(random.nextInt(warehouses.size()));
                    Long idWarehouse = randomWarehouse.getIdWarehouse();

                    // Randomly decide the type of relationship: 1=box only, 2=pack only, 3=both
                    int relationshipType = 1 + random.nextInt(3);
                    
                    Long idBox = null;
                    Long idPack = null;
                    String description = "";

                    if (relationshipType == 1 && !boxes.isEmpty()) {
                        // Only box
                        Box randomBox = boxes.get(random.nextInt(boxes.size()));
                        idBox = randomBox.getIdBox();
                        description = "Box ID: " + idBox;
                        
                        // Check if already exists
                        if (warehouseItemService.isBoxAssignedToWarehouse(idWarehouse, idBox)) {
                            logger.debug("Attempt {}: Box {} already assigned to Warehouse {}, skipping", attempts, idBox, idWarehouse);
                            skippedCount++;
                            continue;
                        }
                        
                        warehouseItemService.assignBoxToWarehouse(idWarehouse, idBox);
                        logger.info("Created warehouse-item relationship {}: Warehouse {} -> {}", createdCount + 1, idWarehouse, description);
                        
                    } else if (relationshipType == 2 && !packs.isEmpty()) {
                        // Only pack
                        Pack randomPack = packs.get(random.nextInt(packs.size()));
                        idPack = randomPack.getIdPack();
                        description = "Pack ID: " + idPack;
                        
                        // Check if already exists
                        if (warehouseItemService.isPackAssignedToWarehouse(idWarehouse, idPack)) {
                            logger.debug("Attempt {}: Pack {} already assigned to Warehouse {}, skipping", attempts, idPack, idWarehouse);
                            skippedCount++;
                            continue;
                        }
                        
                        warehouseItemService.assignPackToWarehouse(idWarehouse, idPack);
                        logger.info("Created warehouse-item relationship {}: Warehouse {} -> {}", createdCount + 1, idWarehouse, description);
                        
                    } else if (relationshipType == 3 && !boxes.isEmpty() && !packs.isEmpty()) {
                        // Both box and pack
                        Box randomBox = boxes.get(random.nextInt(boxes.size()));
                        Pack randomPack = packs.get(random.nextInt(packs.size()));
                        idBox = randomBox.getIdBox();
                        idPack = randomPack.getIdPack();
                        description = "Box ID: " + idBox + ", Pack ID: " + idPack;
                        
                        warehouseItemService.assignBoxAndPackToWarehouse(idWarehouse, idBox, idPack);
                        logger.info("Created warehouse-item relationship {}: Warehouse {} -> {}", createdCount + 1, idWarehouse, description);
                        
                    } else {
                        // Fallback: try box or pack if available
                        if (!boxes.isEmpty()) {
                            Box randomBox = boxes.get(random.nextInt(boxes.size()));
                            idBox = randomBox.getIdBox();
                            if (!warehouseItemService.isBoxAssignedToWarehouse(idWarehouse, idBox)) {
                                warehouseItemService.assignBoxToWarehouse(idWarehouse, idBox);
                                description = "Box ID: " + idBox;
                                logger.info("Created warehouse-item relationship {}: Warehouse {} -> {}", createdCount + 1, idWarehouse, description);
                            } else {
                                skippedCount++;
                                continue;
                            }
                        } else if (!packs.isEmpty()) {
                            Pack randomPack = packs.get(random.nextInt(packs.size()));
                            idPack = randomPack.getIdPack();
                            if (!warehouseItemService.isPackAssignedToWarehouse(idWarehouse, idPack)) {
                                warehouseItemService.assignPackToWarehouse(idWarehouse, idPack);
                                description = "Pack ID: " + idPack;
                                logger.info("Created warehouse-item relationship {}: Warehouse {} -> {}", createdCount + 1, idWarehouse, description);
                            } else {
                                skippedCount++;
                                continue;
                            }
                        } else {
                            logger.warn("No boxes or packs available, skipping warehouse-item creation");
                            skippedCount++;
                            continue;
                        }
                    }
                    
                    createdCount++;
                    
                } catch (RuntimeException e) {
                    // Handle duplicate assignment errors gracefully
                    if (e.getMessage() != null && e.getMessage().contains("already assigned")) {
                        skippedCount++;
                        logger.debug("Attempt {}: Relationship already exists, skipping", attempts);
                    } else {
                        errorCount++;
                        logger.error("Error creating warehouse-item relationship (attempt {}): {}", attempts, e.getMessage());
                    }
                } catch (Exception e) {
                    errorCount++;
                    logger.error("Unexpected error creating warehouse-item relationship (attempt {}): {}", attempts, e.getMessage());
                }
            }

            logger.info("Default warehouse-items initialization completed successfully.");
            logger.info("Results: Created={}, Skipped={}, Errors={}, Total Attempts={}", 
                createdCount, skippedCount, errorCount, attempts);

        } catch (Exception e) {
            logger.error("Error initializing default warehouse-items: {}", e.getMessage(), e);
            // Don't stop the application if warehouse-items initialization fails
        }
    }

    /**
     * Initialize test users for development/testing
     */
    private void initializeTestUsers() {
        try {
            logger.info("Initializing test users...");

            // 1. Create test admin user
            createTestUser(
                "testadmin",
                "testadmin@mamukas.com",
                "admin123",
                "Test",
                "Administrator",
                30,
                "11111111",
                1, // Active
                "Admin"
            );

            // 2. Create test employee user
            createTestUser(
                "testemployee",
                "testemployee@mamukas.com",
                "employee123",
                "Test",
                "Employee",
                28,
                "22222222",
                1, // Active
                "Employee"
            );

            // 3. Create test customer user
            createTestUser(
                "testcustomer",
                "testcustomer@mamukas.com",
                "customer123",
                "Test",
                "Customer",
                25,
                "33333333",
                1, // Active
                "Customer"
            );

            logger.info("Test users initialization completed successfully");
            logger.info("Test users created:");
            logger.info("  - Admin: username='testadmin', password='admin123'");
            logger.info("  - Employee: username='testemployee', password='employee123'");
            logger.info("  - Customer: username='testcustomer', password='customer123'");
            logger.warn("SECURITY WARNING: Test users created with default passwords. Change them in production!");

        } catch (Exception e) {
            logger.error("Error initializing test users: {}", e.getMessage(), e);
            // Don't stop the application if test user creation fails
        }
    }

    /**
     * Helper method to create a test user
     */
    private void createTestUser(String username, String email, String password, 
                                String name, String lastName, Integer age, 
                                String ci, Integer status, String roleName) {
        try {
            // Check if user already exists
            if (userService.existsByUsername(username) || userService.existsByEmail(email)) {
                logger.info("Test user '{}' already exists, skipping creation", username);
                return;
            }

            // Create user
            UserRequestDto userRequest = new UserRequestDto();
            userRequest.setUsername(username);
            userRequest.setEmail(email);
            userRequest.setPassword(password);
            userRequest.setName(name);
            userRequest.setLastName(lastName);
            userRequest.setAge(age);
            userRequest.setCi(ci);
            userRequest.setStatus(status);
            userRequest.setRoleName(roleName);

            userService.createUser(userRequest);
            logger.info("Created test user: {} with role: {}", username, roleName);

        } catch (Exception e) {
            logger.error("Error creating test user '{}': {}", username, e.getMessage());
        }
    }
}
