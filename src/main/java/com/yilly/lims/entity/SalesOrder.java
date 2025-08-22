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
public class SalesOrder {

    @Id
    private String sorderId;

    private String customerId;

    private BigDecimal soPrice;

    private LocalDateTime soTimeStamp;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Transient
    private List<OrderItem> soItem;
}