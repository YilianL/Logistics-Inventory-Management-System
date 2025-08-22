package com.yilly.lims.entity;

import com.yilly.lims.enums.ShipmentStatus;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Shipment {

    @Id
    private Long shipmentId;

    private Long sorderId;

    @Enumerated(EnumType.STRING)
    private ShipmentStatus status;

    private String location;

    private String destination;

    private LocalDateTime departedTime;

    private LocalDateTime estimatedArrival;

    private LocalDateTime lastUpdated;
}