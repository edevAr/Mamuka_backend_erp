-- Remove stock column from products table
-- Stock is now read directly from boxes and packs tables

ALTER TABLE products DROP COLUMN IF EXISTS stock;


