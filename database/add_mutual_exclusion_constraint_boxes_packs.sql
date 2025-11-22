-- This script documents the mutual exclusion constraint between boxes and packs
-- A product can only exist in either boxes OR packs, not both
-- This constraint is enforced at the application level in BoxService and PackService

-- Note: This constraint cannot be enforced directly at the database level using standard SQL constraints
-- because it requires checking across two different tables. However, we can create a trigger
-- or stored procedure to enforce this, or rely on application-level validation.

-- Option 1: Create a trigger to prevent insertion if product exists in the other table
-- (This is optional and can be implemented if needed)

DELIMITER $$

-- Trigger to prevent box insertion if product exists in packs
DROP TRIGGER IF EXISTS prevent_box_if_product_in_packs$$
CREATE TRIGGER prevent_box_if_product_in_packs
BEFORE INSERT ON boxes
FOR EACH ROW
BEGIN
    IF EXISTS (SELECT 1 FROM packs WHERE id_product = NEW.id_product) THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Product already exists in packs table. A product cannot be in both boxes and packs.';
    END IF;
END$$

-- Trigger to prevent pack insertion if product exists in boxes
DROP TRIGGER IF EXISTS prevent_pack_if_product_in_boxes$$
CREATE TRIGGER prevent_pack_if_product_in_boxes
BEFORE INSERT ON packs
FOR EACH ROW
BEGIN
    IF EXISTS (SELECT 1 FROM boxes WHERE id_product = NEW.id_product) THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Product already exists in boxes table. A product cannot be in both boxes and packs.';
    END IF;
END$$

DELIMITER ;

-- Note: If you're using PostgreSQL instead of MySQL, use the following syntax:
/*
-- Trigger function for boxes
CREATE OR REPLACE FUNCTION prevent_box_if_product_in_packs()
RETURNS TRIGGER AS $$
BEGIN
    IF EXISTS (SELECT 1 FROM packs WHERE id_product = NEW.id_product) THEN
        RAISE EXCEPTION 'Product already exists in packs table. A product cannot be in both boxes and packs.';
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Trigger for boxes
CREATE TRIGGER prevent_box_if_product_in_packs
BEFORE INSERT ON boxes
FOR EACH ROW
EXECUTE FUNCTION prevent_box_if_product_in_packs();

-- Trigger function for packs
CREATE OR REPLACE FUNCTION prevent_pack_if_product_in_boxes()
RETURNS TRIGGER AS $$
BEGIN
    IF EXISTS (SELECT 1 FROM boxes WHERE id_product = NEW.id_product) THEN
        RAISE EXCEPTION 'Product already exists in boxes table. A product cannot be in both boxes and packs.';
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Trigger for packs
CREATE TRIGGER prevent_pack_if_product_in_boxes
BEFORE INSERT ON packs
FOR EACH ROW
EXECUTE FUNCTION prevent_pack_if_product_in_boxes();
*/


