-- Script para actualizar el campo stock de los productos existentes
-- Asigna valores aleatorios entre 0 y 100 a los productos que tienen stock NULL

UPDATE products 
SET stock = FLOOR(0 + RAND() * 101) 
WHERE stock IS NULL;

-- Verificar que se actualizaron correctamente
-- SELECT id_product, name, stock FROM products;


