package com.yilly.lims.entity;

import com.yilly.lims.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseOrder {

    @Id
    private Long porderId;

    private BigDecimal poPrice;

    private LocalDateTime poTimeStamp;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Transient // 不直接用 ORM 映射
    private List<OrderItem> poItem; // 需要通过 service 查询对应 orderId
}