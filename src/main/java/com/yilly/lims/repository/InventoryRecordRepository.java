package com.yilly.lims.repository;

import com.yilly.lims.entity.InventoryRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventoryRecordRepository extends JpaRepository<InventoryRecord, Long> {

    List<InventoryRecord> findByProduct_ProductID(String productId);

    List<InventoryRecord> findByOperator_OperatorID(String operatorId);

    List<InventoryRecord> findByCheckStatus(Boolean checkStatus);
}