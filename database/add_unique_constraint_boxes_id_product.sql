-- Add UNIQUE constraint to id_product in boxes table
-- This ensures that each product can only have one box record

-- First, remove any duplicate records (keep the one with the lowest id_box)
DELETE b1 FROM boxes b1
INNER JOIN boxes b2 
WHERE b1.id_product = b2.id_product 
AND b1.id_box > b2.id_box;

-- Add UNIQUE constraint
ALTER TABLE boxes 
ADD CONSTRAINT uk_boxes_id_product UNIQUE (id_product);


