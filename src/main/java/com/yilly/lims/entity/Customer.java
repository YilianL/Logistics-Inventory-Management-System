package com.yilly.lims.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

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

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<SalesOrder> orders;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CustomerFeedback> feedbacks;
}