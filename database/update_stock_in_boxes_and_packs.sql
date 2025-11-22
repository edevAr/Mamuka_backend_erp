-- Update stock for all boxes where stock is NULL or needs recalculation
-- stock = units * units_box
UPDATE boxes 
SET stock = units * units_box 
WHERE (stock IS NULL OR stock != units * units_box)
  AND units IS NOT NULL 
  AND units_box IS NOT NULL;

-- Update stock for all packs where stock is NULL or needs recalculation
-- stock = units * units_pack
UPDATE packs 
SET stock = units * units_pack 
WHERE (stock IS NULL OR stock != units * units_pack)
  AND units IS NOT NULL 
  AND units_pack IS NOT NULL;

-- Set stock to 0 for boxes where units or units_box is NULL
UPDATE boxes 
SET stock = 0 
WHERE stock IS NULL 
  AND (units IS NULL OR units_box IS NULL);

-- Set stock to 0 for packs where units or units_pack is NULL
UPDATE packs 
SET stock = 0 
WHERE stock IS NULL 
  AND (units IS NULL OR units_pack IS NULL);


