package com.yilly.lims.controller;

import com.yilly.lims.entity.InventoryRecord;
import com.yilly.lims.service.InventoryRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/inventory")
public class InventoryRecordController {

    private final InventoryRecordService inventoryRecordService;

    @Autowired
    public InventoryRecordController(InventoryRecordService inventoryRecordService) {
        this.inventoryRecordService = inventoryRecordService;
    }

    @PostMapping("/record")
    public InventoryRecord createRecord(@RequestBody InventoryRecord record) {
        return inventoryRecordService.createRecord(record);
    }

    @GetMapping("/records")
    public List<InventoryRecord> getAllRecords() {
        return inventoryRecordService.getAllRecords();
    }

    @GetMapping("/record/{id}")
    public InventoryRecord getRecordById(@PathVariable Long id) {
        return inventoryRecordService.getRecordById(id)
                .orElseThrow(() -> new RuntimeException("Record not found"));
    }

    @DeleteMapping("/record/{id}")
    public void deleteRecord(@PathVariable Long id) {
        inventoryRecordService.deleteRecord(id);
    }

    @GetMapping("/records/product/{productId}")
    public List<InventoryRecord> getRecordsByProduct(@PathVariable String productId) {
        return inventoryRecordService.getRecordsByProduct(productId);
    }

    @GetMapping("/records/operator/{operatorId}")
    public List<InventoryRecord> getRecordsByOperator(@PathVariable String operatorId) {
        return inventoryRecordService.getRecordsByOperator(operatorId);
    }

    @GetMapping("/records/check-status/{status}")
    public List<InventoryRecord> getRecordsByCheckStatus(@PathVariable Boolean status) {
        return inventoryRecordService.getRecordsByCheckStatus(status);
    }
}