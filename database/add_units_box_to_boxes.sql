-- Script para agregar la columna 'units_box' a la tabla 'boxes'
-- Esta columna almacenará el número de unidades por caja

ALTER TABLE boxes ADD COLUMN IF NOT EXISTS units_box INT DEFAULT NULL;

-- Opcional: Si necesitas establecer un valor por defecto para las filas existentes
-- UPDATE boxes SET units_box = FLOOR(1 + RAND() * 20) WHERE units_box IS NULL;

-- Verificar la estructura de la tabla después del cambio
-- DESCRIBE boxes;


