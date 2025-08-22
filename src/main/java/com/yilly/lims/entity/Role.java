package com.yilly.lims.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roleID;

    @Column(nullable = false, unique = true, length = 64)
    private String roleName; // 如 SUPER_ADMIN / SYSTEM_ADMIN / INVENTORY_ADMIN

    @ManyToMany
    @JoinTable(
            name = "role_permissions",
            joinColumns = @JoinColumn(name = "role_id", referencedColumnName = "roleID"),
            inverseJoinColumns = @JoinColumn(name = "permission_id", referencedColumnName = "permissionID")
    )
    private Set<Permission> permissions = new HashSet<>();
}
