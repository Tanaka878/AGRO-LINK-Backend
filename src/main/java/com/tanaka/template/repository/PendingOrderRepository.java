package com.tanaka.template.repository;

import com.tanaka.template.entity.PendingOrder;
import com.tanaka.template.entity.Farmer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PendingOrderRepository extends JpaRepository<PendingOrder, Long> {
    List<PendingOrder> findByFarmer(Farmer farmer);
}
