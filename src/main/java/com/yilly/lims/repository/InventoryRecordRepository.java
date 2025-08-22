package com.yilly.lims.repository;

import com.yilly.lims.entity.InventoryRecord;
import com.yilly.lims.enums.InventoryType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InventoryRecordRepository extends JpaRepository<InventoryRecord, Long> {
    Optional<InventoryRecord> findByIdAndType(Long recordID, InventoryType type);
}
