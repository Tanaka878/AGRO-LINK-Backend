package com.tanaka.template.repository;

import com.tanaka.template.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByFarmerEmail(String farmerEmail);
    List<Order> findByBuyerEmail(String buyerEmail);
}
