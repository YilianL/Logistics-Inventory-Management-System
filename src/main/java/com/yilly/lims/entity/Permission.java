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

    @Column(nullable = false)
    private String description;

    @OneToOne(mappedBy = "permission")
    private Role role;
}