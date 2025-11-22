package com.mamukas.erp.erpbackend.infrastructure.repositories;

import com.mamukas.erp.erpbackend.domain.entities.WarehouseItem;
import com.mamukas.erp.erpbackend.infrastructure.persistence.jpa.WarehouseItemJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface WarehouseItemRepository extends JpaRepository<WarehouseItemJpaEntity, Long> {
    
    List<WarehouseItemJpaEntity> findByIdWarehouse(Long idWarehouse);
    
    List<WarehouseItemJpaEntity> findByIdBox(Long idBox);
    
    List<WarehouseItemJpaEntity> findByIdPack(Long idPack);
    
    Optional<WarehouseItemJpaEntity> findByIdWarehouseAndIdBox(Long idWarehouse, Long idBox);
    
    Optional<WarehouseItemJpaEntity> findByIdWarehouseAndIdPack(Long idWarehouse, Long idPack);
    
    @Query("SELECT wi FROM WarehouseItemJpaEntity wi WHERE wi.idWarehouse = :idWarehouse")
    List<WarehouseItemJpaEntity> findItemsByWarehouse(@Param("idWarehouse") Long idWarehouse);
    
    @Query("SELECT wi FROM WarehouseItemJpaEntity wi WHERE wi.idBox = :idBox")
    List<WarehouseItemJpaEntity> findWarehousesByBox(@Param("idBox") Long idBox);
    
    @Query("SELECT wi FROM WarehouseItemJpaEntity wi WHERE wi.idPack = :idPack")
    List<WarehouseItemJpaEntity> findWarehousesByPack(@Param("idPack") Long idPack);
    
    @Query("SELECT wi FROM WarehouseItemJpaEntity wi WHERE wi.idWarehouse = :idWarehouse AND (wi.idBox IS NOT NULL OR wi.idPack IS NOT NULL)")
    List<WarehouseItemJpaEntity> findItemsByWarehouseWithBoxOrPack(@Param("idWarehouse") Long idWarehouse);
    
    @Query("SELECT wi FROM WarehouseItemJpaEntity wi WHERE wi.idWarehouse = :idWarehouse AND wi.createdAt BETWEEN :startDate AND :endDate")
    List<WarehouseItemJpaEntity> findByWarehouseAndDateRange(@Param("idWarehouse") Long idWarehouse,
                                                             @Param("startDate") LocalDateTime startDate,
                                                             @Param("endDate") LocalDateTime endDate);
    
    // Domain conversion methods
    default WarehouseItem toDomain(WarehouseItemJpaEntity entity) {
        if (entity == null) return null;
        
        return new WarehouseItem(
            entity.getIdWarehouseItem(),
            entity.getIdWarehouse(),
            entity.getIdBox(),
            entity.getIdPack(),
            entity.getStatus(),
            entity.getCreatedAt(),
            entity.getUpdatedAt()
        );
    }
    
    default WarehouseItemJpaEntity toEntity(WarehouseItem domain) {
        if (domain == null) return null;
        
        WarehouseItemJpaEntity entity = new WarehouseItemJpaEntity();
        entity.setIdWarehouseItem(domain.getIdWarehouseItem());
        entity.setIdWarehouse(domain.getIdWarehouse());
        entity.setIdBox(domain.getIdBox());
        entity.setIdPack(domain.getIdPack());
        entity.setStatus(domain.getStatus());
        entity.setCreatedAt(domain.getCreatedAt());
        entity.setUpdatedAt(domain.getUpdatedAt());
        
        return entity;
    }
}

