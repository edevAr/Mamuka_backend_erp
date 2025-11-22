-- Add status column to warehouse_items table
ALTER TABLE warehouse_items 
ADD COLUMN status VARCHAR(50) NULL;

-- Optional: Update existing records with a default status if needed
-- UPDATE warehouse_items SET status = 'Active' WHERE status IS NULL;


