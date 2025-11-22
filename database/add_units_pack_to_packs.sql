-- Script para agregar la columna 'units_pack' a la tabla 'packs'
-- Esta columna almacenará el número de unidades por paquete

ALTER TABLE packs ADD COLUMN IF NOT EXISTS units_pack INT DEFAULT NULL;

-- Opcional: Si necesitas establecer un valor por defecto para las filas existentes
-- UPDATE packs SET units_pack = FLOOR(1 + RAND() * 15) WHERE units_pack IS NULL;

-- Verificar la estructura de la tabla después del cambio
-- DESCRIBE packs;


