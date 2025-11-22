-- Add stock column to boxes table
ALTER TABLE boxes 
ADD COLUMN IF NOT EXISTS stock INT DEFAULT NULL;

-- Add stock column to packs table
ALTER TABLE packs 
ADD COLUMN IF NOT EXISTS stock INT DEFAULT NULL;

-- Update existing boxes: stock = units * units_box
UPDATE boxes 
SET stock = units * units_box 
WHERE units IS NOT NULL AND units_box IS NOT NULL;

-- Update existing packs: stock = units * units_pack
UPDATE packs 
SET stock = units * units_pack 
WHERE units IS NOT NULL AND units_pack IS NOT NULL;

-- Add index for better query performance
CREATE INDEX IF NOT EXISTS idx_boxes_stock ON boxes(stock);
CREATE INDEX IF NOT EXISTS idx_packs_stock ON packs(stock);


