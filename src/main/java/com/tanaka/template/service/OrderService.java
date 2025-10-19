package com.tanaka.template.service;

import com.tanaka.template.dto.OrderStatus;
import com.tanaka.template.dto.PaymentStatus;
import com.tanaka.template.entity.Order;

import java.util.List;

public interface OrderService {

    Order createOrder(Order order);

    List<Order> getOrdersForFarmer(String farmerEmail);

    List<Order> getAllOrders();

    Order updateOrderStatus(Long orderId, OrderStatus status);

    public List<Order> getOrdersByBuyerEmail(String buyerEmail);
    Order updatePaymentStatus(Long orderId, PaymentStatus paymentStatus);
    void deleteOrder(Long orderId);
}
