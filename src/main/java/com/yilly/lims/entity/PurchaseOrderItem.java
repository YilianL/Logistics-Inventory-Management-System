package com.yilly.lims.entity;

import com.yilly.lims.enums.Unit;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "purchase_order_item")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseOrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long itemID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "porder_id")
    private PurchaseOrder purchaseOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    private BigDecimal quantity;

    @Enumerated(EnumType.STRING)
    private Unit unit;
}