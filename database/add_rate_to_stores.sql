-- Script para agregar la columna rate a la tabla stores
-- Esta columna almacena la puntuación de la tienda entre 1.0 y 5.0

-- Agregar la columna rate si no existe
ALTER TABLE stores 
ADD COLUMN IF NOT EXISTS rate DECIMAL(3,1) DEFAULT NULL;

-- Comentario sobre la columna
-- La columna rate almacena la puntuación de la tienda
-- Valores válidos: entre 1.0 y 5.0
-- NULL significa que la tienda aún no tiene puntuación asignada


