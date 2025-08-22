package com.yilly.lims.repository;

import com.yilly.lims.entity.OperatorLog;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OperatorLogRepository extends JpaRepository<OperatorLog, Long> {
    List<OperatorLog> findByOperator_OperatorID(Long operatorId);
}
