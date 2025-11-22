package com.mamukas.erp.erpbackend.infrastructure.repositories;

import com.mamukas.erp.erpbackend.domain.entities.Pack;
import com.mamukas.erp.erpbackend.infrastructure.persistence.jpa.PackJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PackRepository extends JpaRepository<PackJpaEntity, Long> {
    
    // Custom query methods
    Optional<PackJpaEntity> findByIdPack(Long idPack);
    
    List<PackJpaEntity> findByIdProduct(Long idProduct);
    
    List<PackJpaEntity> findByName(String name);
    
    List<PackJpaEntity> findByNameContaining(String name);
    
    List<PackJpaEntity> findByExpirationDateBefore(LocalDate date);
    
    List<PackJpaEntity> findByExpirationDateAfter(LocalDate date);
    
    List<PackJpaEntity> findByUnitsGreaterThan(Integer units);
    
    @Query("SELECT p FROM PackJpaEntity p WHERE p.expirationDate BETWEEN :startDate AND :endDate")
    List<PackJpaEntity> findByExpirationDateBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    
    @Query("SELECT p FROM PackJpaEntity p WHERE p.units > 0")
    List<PackJpaEntity> findPacksWithStock();
    
    @Query("SELECT p FROM PackJpaEntity p WHERE p.units <= 0")
    List<PackJpaEntity> findPacksOutOfStock();
    
    @Query("SELECT p FROM PackJpaEntity p WHERE p.idProduct = :idProduct AND p.units > 0")
    List<PackJpaEntity> findPacksWithStockByProduct(@Param("idProduct") Long idProduct);
    
    @Query("SELECT SUM(p.units) FROM PackJpaEntity p WHERE p.idProduct = :idProduct")
    Long getTotalUnitsByProduct(@Param("idProduct") Long idProduct);
    
    @Query("SELECT p.idProduct, SUM(p.units) FROM PackJpaEntity p WHERE p.idProduct IN :productIds GROUP BY p.idProduct")
    List<Object[]> getTotalUnitsByProducts(@Param("productIds") List<Long> productIds);
    
    @Query("SELECT p.stock FROM PackJpaEntity p WHERE p.idProduct = :idProduct AND p.stock IS NOT NULL ORDER BY p.idPack LIMIT 1")
    Integer getStockByProduct(@Param("idProduct") Long idProduct);
    
    @Query("SELECT p.idProduct, p.stock FROM PackJpaEntity p WHERE p.idProduct IN :productIds AND p.stock IS NOT NULL")
    List<Object[]> getStockByProducts(@Param("productIds") List<Long> productIds);
    
    @Query("SELECT COALESCE(SUM(p.stock), 0) FROM PackJpaEntity p WHERE p.idProduct = :idProduct")
    Long getTotalStockByProduct(@Param("idProduct") Long idProduct);
    
    @Query("SELECT p.idProduct, COALESCE(SUM(p.stock), 0) FROM PackJpaEntity p WHERE p.idProduct IN :productIds GROUP BY p.idProduct")
    List<Object[]> getTotalStockByProducts(@Param("productIds") List<Long> productIds);
    
    // Conversion methods
    default Pack toDomain(PackJpaEntity entity) {
        return new Pack(
            entity.getIdPack(),
            entity.getIdProduct(),
            entity.getName(),
            entity.getExpirationDate(),
            entity.getUnits(),
            entity.getUnitsPack(),
            entity.getStock(),
            entity.getCreatedAt(),
            entity.getUpdatedAt()
        );
    }
    
    default PackJpaEntity toEntity(Pack domain) {
        PackJpaEntity entity = new PackJpaEntity();
        entity.setIdPack(domain.getIdPack());
        entity.setIdProduct(domain.getIdProduct());
        entity.setName(domain.getName());
        entity.setExpirationDate(domain.getExpirationDate());
        entity.setUnits(domain.getUnits());
        entity.setUnitsPack(domain.getUnitsPack());
        entity.setStock(domain.getStock());
        entity.setCreatedAt(domain.getCreatedAt());
        entity.setUpdatedAt(domain.getUpdatedAt());
        return entity;
    }
}
