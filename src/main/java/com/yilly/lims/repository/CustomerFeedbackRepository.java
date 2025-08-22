package com.yilly.lims.repository;

import com.yilly.lims.entity.CustomerFeedback;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerFeedbackRepository extends JpaRepository<CustomerFeedback, Long> {
}