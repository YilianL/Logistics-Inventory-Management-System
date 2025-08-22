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
    private String shipmentCostId;

    private String shipmentId;

    private BigDecimal cost;

    private String costType;
}