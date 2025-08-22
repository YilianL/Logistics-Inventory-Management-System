package com.yilly.lims.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Role {

    @Id
    private Long roleID;

    @Column(nullable = false, unique = true)
    private String roleName;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "permission_id", referencedColumnName = "permissionId")
    private Permission permission;

    @OneToMany(mappedBy = "role")
    private List<Operator> operators;
}