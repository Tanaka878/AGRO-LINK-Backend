package com.tanaka.template.controller;

import com.tanaka.template.dto.FarmerRequest;
import com.tanaka.template.dto.OrderStatus;
import com.tanaka.template.entity.Order;
import com.tanaka.template.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/create")
    public ResponseEntity<Order> createOrder(@RequestBody Order order) {
        Order savedOrder = orderService.createOrder(order);
        return new ResponseEntity<>(savedOrder, HttpStatus.CREATED);
    }

    @PostMapping("/farmer")
    public ResponseEntity<List<Order>> getOrdersForFarmer(@RequestBody FarmerRequest request) {
        List<Order> orders = orderService.getOrdersForFarmer(request.getEmail());
        return ResponseEntity.ok(orders);
    }


    @GetMapping("/all")
    public ResponseEntity<List<Order>> getAllOrders() {
        List<Order> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    @PutMapping("/update-status/{orderId}")
    public ResponseEntity<Order> updateOrderStatus(@PathVariable Long orderId, @RequestParam OrderStatus status) {
        Order updatedOrder = orderService.updateOrderStatus(orderId, status);
        return ResponseEntity.ok(updatedOrder);
    }
    @GetMapping("/buyer/{email}")
    public ResponseEntity<List<Order>> getOrdersByBuyerEmail(@PathVariable String email) {
        List<Order> orders = orderService.getOrdersByBuyerEmail(email);
        if (orders.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(orders);
    }

    @PutMapping("/cancel/{orderId}")
    public ResponseEntity<Order> cancelOrder(@PathVariable Long orderId) {
        Order cancelledOrder = orderService.updateOrderStatus(orderId, OrderStatus.CANCELLED);
        return ResponseEntity.ok(cancelledOrder);
    }



}
