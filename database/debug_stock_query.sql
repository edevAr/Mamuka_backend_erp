-- Debug query to check stock for product ID 9
-- Check boxes for product 9
SELECT 
    id_box,
    id_product,
    units,
    units_box,
    stock,
    (units * units_box) as calculated_stock
FROM boxes 
WHERE id_product = 9;

-- Check packs for product 9
SELECT 
    id_pack,
    id_product,
    units,
    units_pack,
    stock,
    (units * units_pack) as calculated_stock
FROM packs 
WHERE id_product = 9;

-- Sum of stock from boxes for product 9
SELECT COALESCE(SUM(stock), 0) as total_stock_boxes
FROM boxes 
WHERE id_product = 9 AND stock IS NOT NULL;

-- Sum of stock from packs for product 9
SELECT COALESCE(SUM(stock), 0) as total_stock_packs
FROM packs 
WHERE id_product = 9 AND stock IS NOT NULL;

-- Total stock (boxes + packs) for product 9
SELECT 
    COALESCE((SELECT SUM(stock) FROM boxes WHERE id_product = 9 AND stock IS NOT NULL), 0) +
    COALESCE((SELECT SUM(stock) FROM packs WHERE id_product = 9 AND stock IS NOT NULL), 0) 
    as total_stock;


