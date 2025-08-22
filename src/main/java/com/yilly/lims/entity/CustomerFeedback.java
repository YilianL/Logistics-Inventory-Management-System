package com.yilly.lims.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "customer_feedback")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerFeedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long feedbackID;

    private Long customerID;

    private Long sorderID;

    private Integer rating;

    private String feedbackText;

    private LocalDateTime submittedTime;
}