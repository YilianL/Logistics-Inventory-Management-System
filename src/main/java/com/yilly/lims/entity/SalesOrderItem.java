package com.yilly.lims.entity;

import com.yilly.lims.enums.Unit;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "sales_order_item")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SalesOrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long itemID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sorder_id")
    private SalesOrder salesOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    private BigDecimal quantity;

    @Enumerated(EnumType.STRING)
    private Unit unit;
}