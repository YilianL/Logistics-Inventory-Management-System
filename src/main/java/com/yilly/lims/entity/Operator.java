package com.yilly.lims.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor
@Table(name = "operators")
public class Operator {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long operatorID;

    @Column(unique = true, nullable = false, length = 64)
    private String username;

    @Column(nullable = false)
    private String password; // 存BCrypt密文

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    private Role role;       // 所属角色
}
