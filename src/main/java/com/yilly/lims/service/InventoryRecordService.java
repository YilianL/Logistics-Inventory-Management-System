package com.yilly.lims.service;

import com.yilly.lims.entity.InventoryRecord;
import com.yilly.lims.enums.InventoryType;
import com.yilly.lims.enums.Unit;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface InventoryRecordService {

    // Inbound
    Long createInbound(Long productID,
                       BigDecimal quantity,
                       Unit unit,
                       Boolean check,
                       Long operatorID,
                       LocalDateTime operatedTime);

    InventoryRecord getInbound(Long recordID);

    void updateInbound(Long recordID,
                       Long productID,
                       BigDecimal quantity,
                       Unit unit,
                       Boolean check,
                       Long operatorID,
                       LocalDateTime operatedTime);

    void deleteInbound(Long recordID);

    // Outbound
    Long createOutbound(Long productID,
                        BigDecimal quantity,
                        Unit unit,
                        Boolean check,
                        Long operatorID,
                        LocalDateTime operatedTime);

    InventoryRecord getOutbound(Long recordID);

    void updateOutbound(Long recordID,
                        Long productID,
                        BigDecimal quantity,
                        Unit unit,
                        Boolean check,
                        Long operatorID,
                        LocalDateTime operatedTime);

    void deleteOutbound(Long recordID);
}
