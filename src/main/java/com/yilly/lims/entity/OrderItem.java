package com.yilly.lims.entity;

import com.yilly.lims.enums.Unit;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {

    @Id
    private Long itemId;

    private Long orderId; // 对应 SalesOrder 或 PurchaseOrder 的 ID

    private Long productId;

    private BigDecimal quantity;

    @Enumerated(EnumType.STRING)
    private Unit unit;
}