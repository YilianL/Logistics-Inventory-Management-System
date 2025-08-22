package com.yilly.lims.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Permission {

    @Id
    private Long permissionId;

    @Column(nullable = false, unique = true)
    private String permissionName;

    private String description;

    private String resource;

    private String action;

    @OneToOne(mappedBy = "permission")
    private Role role;
}