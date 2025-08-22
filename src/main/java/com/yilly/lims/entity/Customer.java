package com.yilly.lims.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "customers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerID;

    private String name;

    private String email;

    private String phone;

    private String address;

    private Integer level;
}