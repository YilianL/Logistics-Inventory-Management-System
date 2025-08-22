package com.yilly.lims.entity;

import com.yilly.lims.enums.InventoryType;
import com.yilly.lims.enums.Unit;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryRecord {

    @Id
    private Long recordId;

    private Long productId;

    private BigDecimal quantity;

    @Enumerated(EnumType.STRING)
    private Unit unit;

    @Enumerated(EnumType.STRING)
    private InventoryType type; // IN or OUT

    private Boolean checkStatus;

    private Long operatorId;

    private LocalDateTime operatedTime;
}