package com.yilly.lims.service;

import com.yilly.lims.entity.InventoryRecord;
import com.yilly.lims.repository.InventoryRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InventoryRecordService {

    private final InventoryRecordRepository inventoryRecordRepository;

    @Autowired
    public InventoryRecordService(InventoryRecordRepository inventoryRecordRepository) {
        this.inventoryRecordRepository = inventoryRecordRepository;
    }

    public InventoryRecord createRecord(InventoryRecord record) {
        return inventoryRecordRepository.save(record);
    }

    public List<InventoryRecord> getAllRecords() {
        return inventoryRecordRepository.findAll();
    }

    public Optional<InventoryRecord> getRecordById(Long id) {
        return inventoryRecordRepository.findById(id);
    }

    public void deleteRecord(Long id) {
        inventoryRecordRepository.deleteById(id);
    }

    public List<InventoryRecord> getRecordsByProduct(String productId) {
        return inventoryRecordRepository.findByProduct_ProductID(productId);
    }

    public List<InventoryRecord> getRecordsByOperator(String operatorId) {
        return inventoryRecordRepository.findByOperator_OperatorID(operatorId);
    }

    public List<InventoryRecord> getRecordsByCheckStatus(Boolean status) {
        return inventoryRecordRepository.findByCheckStatus(status);
    }
}