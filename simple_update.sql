-- Script simple para actualizar usuarios existentes
-- Ejecutar después de que Hibernate haya creado las columnas automáticamente

UPDATE users 
SET two_factor_enabled = false 
WHERE two_factor_enabled IS NULL;
