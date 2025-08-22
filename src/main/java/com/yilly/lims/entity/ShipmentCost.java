package com.yilly.lims.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShipmentCost {

    @Id
    private Long shipmentCostId;

    private BigDecimal cost;

    private String costType;

    @ManyToOne
    @JoinColumn(name = "shipment_id")
    private Shipment shipment;
}