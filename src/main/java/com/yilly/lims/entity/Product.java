package com.yilly.lims.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    private String productId; // 商品编号，主键

    private String productName; // 商品名称

    private BigDecimal stock; // 当前库存统计

    private BigDecimal salesPrice; // 销售单价

    private BigDecimal minThreshold; // 最低库存阈值

    private BigDecimal maxThreshold; // 最高库存阈值

    @Enumerated(EnumType.STRING)
    private Product.Unit unit;

    // 枚举定义
    public enum Unit {
        PIECE, BOX, BOTTLE, PACK, KG, G, L, ML, METER, SET
    }
}