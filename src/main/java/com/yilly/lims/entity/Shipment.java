package com.yilly.lims.entity;

import com.yilly.lims.enums.ShipmentStatus;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Shipment {

    @Id
    private Long shipmentId;

    @OneToOne
    @JoinColumn(name = "sorder_id", referencedColumnName = "sorderId")
    private SalesOrder salesOrder;

    @Enumerated(EnumType.STRING)
    private ShipmentStatus status;

    private String location;

    private String destination;

    private LocalDateTime departedTime;

    private LocalDateTime estimatedArrival;

    private LocalDateTime lastUpdated;

    @OneToMany(mappedBy = "shipment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ShipmentCost> shipmentCosts;
}