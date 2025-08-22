package com.yilly.lims.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor
@Table(name = "permissions")
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long permissionID;

    @Column(nullable = false, unique = true, length = 64)
    private String permissionName; // 规范：resource.action 如 purchase.approve

    @Column(length = 128)
    private String description;

    @Column(length = 64)
    private String resource; // purchase / sales / product ...

    @Column(length = 32)
    private String action;   // approve / read / create / update / delete
}
