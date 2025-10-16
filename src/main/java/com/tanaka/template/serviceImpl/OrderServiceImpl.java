package com.tanaka.template.serviceImpl;

import com.tanaka.template.entity.Order;
import com.tanaka.template.repository.OrderRepository;
import com.tanaka.template.service.OrderService;
import com.tanaka.template.util.MailSenderService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    //functionality for tracking and cancelling orders;

    private final OrderRepository orderRepository;
    private final MailSenderService mailSenderService;


    public OrderServiceImpl(OrderRepository orderRepository, MailSenderService mailSenderService) {
        this.orderRepository = orderRepository;
        this.mailSenderService = mailSenderService;
    }

    @Override
    public Order createOrder(Order order) {
        mailSenderService.farmerOrderCreationNotification(order);
        mailSenderService.customerOrderCreationNotification(order);

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
