-- Add UNIQUE constraint to id_product in packs table
-- This ensures that each product can only have one pack record

-- First, remove any duplicate records (keep the one with the lowest id_pack)
DELETE p1 FROM packs p1
INNER JOIN packs p2 
WHERE p1.id_product = p2.id_product 
AND p1.id_pack > p2.id_pack;

-- Add UNIQUE constraint
ALTER TABLE packs 
ADD CONSTRAINT uk_packs_id_product UNIQUE (id_product);


