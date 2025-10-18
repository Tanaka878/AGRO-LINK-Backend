package com.tanaka.template.serviceImpl;

import com.tanaka.template.dto.*;
import com.tanaka.template.entity.Comment;
import com.tanaka.template.entity.Farmer;
import com.tanaka.template.entity.ListedProducts;
import com.tanaka.template.entity.Order;
import com.tanaka.template.repository.FarmerRepository;
import com.tanaka.template.service.FarmerService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FarmerServiceImpl implements FarmerService {

    private final FarmerRepository farmerRepository;
    private final PasswordEncoder passwordEncoder;
    private final ListedProductsServiceImpl listedProductsService;
    private final OrderServiceImpl orderService;

    public FarmerServiceImpl(FarmerRepository farmerRepository, PasswordEncoder passwordEncoder, ListedProductsServiceImpl listedProductsService,
                             OrderServiceImpl orderService) {
        this.farmerRepository = farmerRepository;
        this.passwordEncoder = passwordEncoder;
        this.listedProductsService = listedProductsService;
        this.orderService = orderService;
    }

    @Override
    public Farmer registerFarmer(FarmerRegistrationDTO dto) {

        if (farmerRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Farmer with email " + dto.getEmail() + " already exists.");
        }

        // Create a new Farmer object
        Farmer farmer = new Farmer();

        // Set User fields
        farmer.setFullname(dto.getFullname());
        farmer.setEmail(dto.getEmail());
        farmer.setPassword(passwordEncoder.encode(dto.getPassword()));
        farmer.setGender(dto.getGender());
        farmer.setRole(Role.FARMER); // force role as FARMER

        // Set Farmer-specific fields
        farmer.setFarmName(dto.getFarmName());
        farmer.setFarmLocation(dto.getFarmLocation());
        farmer.setFarmSize(dto.getFarmSize());
        farmer.setFarmingMethods(dto.getFarmingMethods());
        farmer.setExperienceLevel(dto.getExperienceLevel());
        farmer.setCropsGrown(dto.getCropsGrown());
        farmer.setLivestockOwned(dto.getLivestockOwned());
        farmer.setEquipmentAvailable(dto.getEquipmentAvailable());
        farmer.setCertifications(dto.getCertifications());
        farmer.setMarketPreferences(dto.getMarketPreferences());

        // Save to database
        return farmerRepository.save(farmer);
    }

    @Override
    public Farmer getFarmerByEmail(String email) {
        return farmerRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Farmer not found with email: " + email));
    }



    @Override
    public ResponseEntity<FarmerStatistics> getStatistics(String email) {
        FarmerStatistics statistics = new FarmerStatistics();

        List<Order> ordersForFarmer = orderService.getOrdersForFarmer(email);
        if (ordersForFarmer == null) {
            ordersForFarmer = new ArrayList<>();
        }

        long pendingOrders = ordersForFarmer.stream().filter(o -> "PENDING".equals(o.getStatus())).count();
        long completedOrders = ordersForFarmer.stream().filter(o -> "COMPLETED".equals(o.getStatus())).count();
        long cancelledOrders = ordersForFarmer.stream().filter(o -> "CANCELLED".equals(o.getStatus())).count();

        statistics.setPendingOrders(pendingOrders);
        statistics.setCompletedOrders(completedOrders);
        statistics.setCancelledOrders(cancelledOrders);

        // You already have listed products logic — keep it
        List<ListedProductsDTO> listedProducts = listedProductsService.getAllListedProductsWithComments();
        statistics.setListedProducts(listedProducts.size());

        return ResponseEntity.ok(statistics);
    }

    public ResponseEntity<String> addCommentForFarmer(String farmerEmail, CommentDTO dto) {
        Optional<Farmer> farmerOptional = farmerRepository.findByEmail(farmerEmail);

        if (farmerOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Farmer farmer = farmerOptional.get();

        Comment comment = Comment.builder()
                .authorName(dto.getAuthorName())
                .content(dto.getContent())
                .createdAt(LocalDateTime.now())
                .farmer(farmer)
                .build();

        // Add comment directly to farmer's list
        farmer.getComments().add(comment);

        // Save farmer, cascading persists the comment
        farmerRepository.save(farmer);

        return ResponseEntity.ok("Comment saved");
    }



}
