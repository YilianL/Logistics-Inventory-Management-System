package com.yilly.lims.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor
@Table(name = "operator_logs")
public class OperatorLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long logId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "operator_id")
    private Operator operator; // 允许为 null（系统动作/初始化）

    @Column(nullable = false, length = 64)
    private String action;     // LOGIN / LOGOUT / CREATE_ROLE / ASSIGN_PERMISSION / REGISTER ...

    @Column(length = 64)
    private String resource;   // operator / role / permission / purchase / sales

    @Column(length = 256)
    private String details;    // 详细描述（如 role=INVENTORY_ADMIN）

    @Column(nullable = false, updatable = false)
    private Instant timestamp;

    @PrePersist
    public void prePersist() {
        if (timestamp == null) timestamp = Instant.now(); // 插入前自动填充时间
    }
}
