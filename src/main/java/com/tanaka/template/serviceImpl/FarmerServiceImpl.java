package com.tanaka.template.serviceImpl;

import com.tanaka.template.dto.FarmerRegistrationDTO;
import com.tanaka.template.dto.FarmerStatistics;
import com.tanaka.template.dto.Role;
import com.tanaka.template.entity.Farmer;
import com.tanaka.template.entity.Order;
import com.tanaka.template.repository.FarmerRepository;
import com.tanaka.template.service.FarmerService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

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
        var statistics = new FarmerStatistics();
        List<Order> ordersForFarmer = orderService.getOrdersForFarmer(email);
        long pendingOrders = ordersForFarmer == null ? 0 :
                ordersForFarmer.stream()
                        .filter(order -> "PENDING".equals(order.getStatus()))
                        .count();
        statistics.setPendingOrders(pendingOrders);

        return null;
    }
}
