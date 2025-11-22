-- Script para agregar la columna 'name' a la tabla 'warehouses'
-- Esta columna almacenará el nombre del almacén

ALTER TABLE warehouses ADD COLUMN IF NOT EXISTS name VARCHAR(255) NOT NULL DEFAULT '';

-- Opcional: Si necesitas establecer nombres por defecto para las filas existentes
-- UPDATE warehouses SET name = CONCAT('Almacén ', id_warehouse) WHERE name = '' OR name IS NULL;

-- Verificar la estructura de la tabla después del cambio
-- DESCRIBE warehouses;


