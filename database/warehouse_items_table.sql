-- Script SQL para crear la tabla warehouse_items
-- Esta tabla relaciona almacenes con items (boxes y/o packs)

CREATE TABLE IF NOT EXISTS warehouse_items (
    id_warehouse_item BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_warehouse BIGINT NOT NULL,
    id_box BIGINT NULL,
    id_pack BIGINT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (id_warehouse) REFERENCES warehouses(id_warehouse) ON DELETE CASCADE,
    FOREIGN KEY (id_box) REFERENCES boxes(id_box) ON DELETE CASCADE,
    FOREIGN KEY (id_pack) REFERENCES packs(id_pack) ON DELETE CASCADE,
    -- Constraint: al menos uno de id_box o id_pack debe estar presente
    CHECK (id_box IS NOT NULL OR id_pack IS NOT NULL),
    -- Índices para optimizar consultas
    INDEX idx_warehouse_items_warehouse (id_warehouse),
    INDEX idx_warehouse_items_box (id_box),
    INDEX idx_warehouse_items_pack (id_pack)
);

-- Comentarios sobre el diseño:
-- 1. La tabla warehouse_items almacena la relación entre almacenes e items (boxes/packs)
-- 2. Un registro puede tener id_box, id_pack, o ambos
-- 3. Al menos uno de id_box o id_pack debe estar presente (CHECK constraint)
-- 4. Las foreign keys garantizan integridad referencial
-- 5. Se incluyen índices para optimizar consultas frecuentes
-- 6. ON DELETE CASCADE asegura que si se elimina un warehouse, box o pack, se eliminen las relaciones


