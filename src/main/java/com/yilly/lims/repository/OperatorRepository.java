package com.yilly.lims.repository;

import com.yilly.lims.entity.Operator;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface OperatorRepository extends JpaRepository<Operator, Long> {
    Optional<Operator> findByUsername(String username);
}
