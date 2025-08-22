package com.yilly.lims.repository;

import com.yilly.lims.entity.ShipmentCost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShipmentCostRepository extends JpaRepository<ShipmentCost, Long> {
}