package com.tanaka.template.controller;

import com.tanaka.template.dto.FarmerRegistrationDTO;
import com.tanaka.template.dto.FarmerStatistics;
import com.tanaka.template.entity.Farmer;
import com.tanaka.template.entity.PendingOrder;
import com.tanaka.template.repository.FarmerRepository;
import com.tanaka.template.service.FarmerService;
import com.tanaka.template.service.PendingOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/farmers")
@RequiredArgsConstructor
public class FarmerController {

    private final FarmerService farmerService;
    private final PendingOrderService pendingOrderService;
    private final FarmerRepository farmerRepository;

    // Registration endpoint
    @PostMapping("/register")
    public ResponseEntity<Farmer> registerFarmer(@RequestBody FarmerRegistrationDTO dto) {
        Farmer savedFarmer = farmerService.registerFarmer(dto);
        return new ResponseEntity<>(savedFarmer, HttpStatus.CREATED);
    }



    @PostMapping("/create/{farmerId}")
    public ResponseEntity<PendingOrder> createOrder(@PathVariable Long farmerId, @RequestBody PendingOrder order) {
        Optional<Farmer> farmerOptional = farmerRepository.findById(farmerId);

        if (farmerOptional.isEmpty()) {
            // Farmer not found, return 404
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        // Farmer exists
        order.setFarmer(farmerOptional.get());
        PendingOrder savedOrder = pendingOrderService.createOrder(order);
        return new ResponseEntity<>(savedOrder, HttpStatus.CREATED);
    }

    // Get all pending orders for a farmer
    @GetMapping("/farmer/{farmerId}")
    public ResponseEntity<List<PendingOrder>> getPendingOrders(@PathVariable Long farmerId) {
        Optional<Farmer> farmerOptional = farmerRepository.findById(farmerId);

        if (farmerOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        List<PendingOrder> orders = pendingOrderService.getPendingOrdersForFarmer(farmerOptional.get());
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/statistics/{email}")
    public ResponseEntity<FarmerStatistics> getStatistics(@PathVariable String email) {
        FarmerStatistics stats = farmerService.getStatistics(email).getBody();
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/getByEmail/{email}")
    public Farmer getFarmerByEmail(@PathVariable String email) {
        return farmerService.getFarmerByEmail(email);
    }


}
