package com.yilly.lims.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yilly.lims.enums.Unit;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productID; // 商品编号，主键

    private String productName; // 商品名称

    private BigDecimal stock; // 当前库存统计

    private BigDecimal salesPrice; // 销售单价

    private BigDecimal minThreshold; // 最低库存阈值

    private BigDecimal maxThreshold; // 最高库存阈值

    @Enumerated(EnumType.STRING)
    private Unit unit;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<SalesOrderItem> salesOrderItems;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<PurchaseOrderItem> purchaseOrderItems;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<InventoryRecord> inventoryRecords;
}