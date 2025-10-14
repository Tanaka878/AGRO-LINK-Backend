package com.tanaka.template.service;

import com.tanaka.template.entity.Order;

import java.util.List;

public interface OrderService {

    Order createOrder(Order order);

    List<Order> getOrdersForFarmer(String farmerEmail);

    List<Order> getAllOrders();
}
