-- Script para agregar columnas 2FA y actualizar usuarios existentes
-- Ejecutar este script en tu base de datos PostgreSQL

-- 1. Agregar columna two_factor_enabled si no existe
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.columns 
        WHERE table_schema = 'public' 
        AND table_name = 'users' 
        AND column_name = 'two_factor_enabled'
    ) THEN
        ALTER TABLE users ADD COLUMN two_factor_enabled BOOLEAN NOT NULL DEFAULT false;
        RAISE NOTICE 'Columna two_factor_enabled agregada';
    ELSE
        RAISE NOTICE 'Columna two_factor_enabled ya existe';
    END IF;
END $$;

-- 2. Agregar columna two_factor_secret si no existe
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.columns 
        WHERE table_schema = 'public' 
        AND table_name = 'users' 
        AND column_name = 'two_factor_secret'
    ) THEN
        ALTER TABLE users ADD COLUMN two_factor_secret VARCHAR(255);
        RAISE NOTICE 'Columna two_factor_secret agregada';
    ELSE
        RAISE NOTICE 'Columna two_factor_secret ya existe';
    END IF;
END $$;

-- 3. Actualizar usuarios existentes para asegurar que two_factor_enabled sea false
UPDATE users 
SET two_factor_enabled = false 
WHERE two_factor_enabled IS NULL;

-- 4. Verificar resultados
SELECT 
    id_user, 
    username, 
    email, 
    two_factor_enabled, 
    two_factor_secret 
FROM users 
LIMIT 5;

