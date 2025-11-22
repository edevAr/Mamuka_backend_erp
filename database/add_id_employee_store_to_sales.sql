-- Add id_employee_store column to sales table
ALTER TABLE sales 
ADD COLUMN IF NOT EXISTS id_employee_store BIGINT DEFAULT NULL;

-- Add foreign key constraint (optional, uncomment if you want referential integrity)
-- ALTER TABLE sales 
-- ADD CONSTRAINT fk_sales_employee_store 
-- FOREIGN KEY (id_employee_store) REFERENCES employee_stores(id_employee_store) 
-- ON DELETE SET NULL;

-- Add index for better query performance
CREATE INDEX IF NOT EXISTS idx_sales_id_employee_store ON sales(id_employee_store);


