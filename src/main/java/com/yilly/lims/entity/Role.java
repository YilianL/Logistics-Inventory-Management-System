package com.yilly.lims.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Role {

    @Id
    private String roleId;

    @Column(nullable = false, unique = true)
    private String roleName;

    @OneToOne
    @JoinColumn(name = "permission_id")
    private Permission permission;
}