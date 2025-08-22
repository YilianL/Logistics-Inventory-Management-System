package com.yilly.lims.repository;

import com.yilly.lims.entity.InventoryRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryRecordRepository extends JpaRepository<InventoryRecord, Long> {
}