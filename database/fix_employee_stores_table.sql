-- Script para eliminar la columna id_employee de la tabla employee_stores
-- Esta columna no debería existir según el diseño de la tabla

-- Eliminar la columna id_employee si existe
ALTER TABLE employee_stores DROP COLUMN IF EXISTS id_employee;

-- Verificar la estructura de la tabla después del cambio
-- La tabla debería tener solo:
-- - id_employee_store (PK)
-- - id_user (FK a users)
-- - id_store (FK a stores)
-- - created_at
-- - updated_at


