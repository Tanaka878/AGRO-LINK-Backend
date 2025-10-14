package com.tanaka.template.serviceImpl;

import com.tanaka.template.entity.Order;
import com.tanaka.template.repository.OrderRepository;
import com.tanaka.template.service.OrderService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public Order createOrder(Order order) {
        return orderRepository.save(order);
    }

    @Override
    public List<Order> getOrdersForFarmer(String farmerEmail) {
        return orderRepository.findByFarmerEmail(farmerEmail);
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
}
