-- Agregar columnas 2FA si no existen
DO $$
BEGIN
    -- Agregar columna two_factor_enabled si no existe
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.columns 
        WHERE table_name = 'users' AND column_name = 'two_factor_enabled'
    ) THEN
        ALTER TABLE users ADD COLUMN two_factor_enabled BOOLEAN NOT NULL DEFAULT false;
    END IF;
    
    -- Agregar columna two_factor_secret si no existe
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.columns 
        WHERE table_name = 'users' AND column_name = 'two_factor_secret'
    ) THEN
        ALTER TABLE users ADD COLUMN two_factor_secret VARCHAR(255);
    END IF;
END $$;

-- Actualizar usuarios existentes para que tengan 2FA deshabilitado por defecto
UPDATE users 
SET two_factor_enabled = false 
WHERE two_factor_enabled IS NULL;
