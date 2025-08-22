package com.yilly.lims.controller;

import com.yilly.lims.entity.InventoryRecord;
import com.yilly.lims.entity.Operator;
import com.yilly.lims.entity.Product;
import com.yilly.lims.enums.InventoryType;
import com.yilly.lims.enums.Unit;
import com.yilly.lims.repository.InventoryRecordRepository;
import com.yilly.lims.repository.OperatorRepository;
import com.yilly.lims.repository.ProductRepository;
import com.yilly.lims.service.InventoryRecordService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class InventoryRecordController implements InventoryRecordService {

    private final InventoryRecordRepository inventoryRecordRepository;
    private final ProductRepository productRepository;
    private final OperatorRepository operatorRepository;

    // IN
    @Override
    public Long createInbound(Long productID, BigDecimal quantity, Unit unit, Boolean check, Long operatorID, LocalDateTime operatedTime) {
        return create(InventoryType.IN, productID, quantity, unit, check, operatorID, operatedTime);
    }

    @Override
    @Transactional(readOnly = true)
    public InventoryRecord getInbound(Long recordID) {
        return getByIdAndTypeOr404(recordID, InventoryType.IN);
    }

    @Override
    public void updateInbound(Long recordID, Long productID, BigDecimal quantity, Unit unit, Boolean check, Long operatorID, LocalDateTime operatedTime) {
        update(recordID, InventoryType.IN, productID, quantity, unit, check, operatorID, operatedTime);
    }

    @Override
    public void deleteInbound(Long recordID) {
        delete(recordID, InventoryType.IN);
    }

    // OUT
    @Override
    public Long createOutbound(Long productID, BigDecimal quantity, Unit unit, Boolean check, Long operatorID, LocalDateTime operatedTime) {
        return create(InventoryType.OUT, productID, quantity, unit, check, operatorID, operatedTime);
    }

    @Override
    @Transactional(readOnly = true)
    public InventoryRecord getOutbound(Long recordID) {
        return getByIdAndTypeOr404(recordID, InventoryType.OUT);
    }

    @Override
    public void updateOutbound(Long recordID, Long productID, BigDecimal quantity, Unit unit, Boolean check, Long operatorID, LocalDateTime operatedTime) {
        update(recordID, InventoryType.OUT, productID, quantity, unit, check, operatorID, operatedTime);
    }

    @Override
    public void deleteOutbound(Long recordID) {
        delete(recordID, InventoryType.OUT);
    }

    // kernel
    private Long create(InventoryType type,
                        Long productID,
                        BigDecimal quantity,
                        Unit unit,
                        Boolean check,
                        Long operatorID,
                        LocalDateTime operatedTime) {

        Product product = productRepository.findById(productID)
                .orElseThrow(() -> notFound("Product", productID));
        Operator operator = operatorRepository.findById(operatorID)
                .orElseThrow(() -> notFound("Operator", operatorID));

        InventoryRecord record = new InventoryRecord();
        record.setProduct(product);
        record.setQuantity(quantity);
        record.setUnit(unit);
        record.setType(type);
        record.setCheckStatus(check);
        record.setOperator(operator);
        record.setOperatedTime(operatedTime != null ? operatedTime : LocalDateTime.now());

        return inventoryRecordRepository.save(record).getRecordID();
    }

    private InventoryRecord getByIdAndTypeOr404(Long recordID, InventoryType type) {
        return inventoryRecordRepository.findByRecordIDAndType(recordID, type)
                .orElseThrow(() -> notFound("InventoryRecord", recordID));
    }

    private void update(Long recordID,
                        InventoryType type,
                        Long productID,
                        BigDecimal quantity,
                        Unit unit,
                        Boolean check,
                        Long operatorID,
                        LocalDateTime operatedTime) {

        InventoryRecord record = getByIdAndTypeOr404(recordID, type);

        if (productID != null) {
            Product product = productRepository.findById(productID)
                    .orElseThrow(() -> notFound("Product", productID));
            record.setProduct(product);
        }
        if (operatorID != null) {
            Operator operator = operatorRepository.findById(operatorID)
                    .orElseThrow(() -> notFound("Operator", operatorID));
            record.setOperator(operator);
        }
        if (quantity != null) record.setQuantity(quantity);
        if (unit != null) record.setUnit(unit);
        if (check != null) record.setCheckStatus(check);
        if (operatedTime != null) record.setOperatedTime(operatedTime);

        inventoryRecordRepository.save(record);
    }

    private void delete(Long recordID, InventoryType type) {
        InventoryRecord record = getByIdAndTypeOr404(recordID, type);
        inventoryRecordRepository.delete(record);
    }

    private ResponseStatusException notFound(String entity, Long id) {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, entity + " not found: " + id);
    }
}
