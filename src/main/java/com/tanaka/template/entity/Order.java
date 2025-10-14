package com.tanaka.template.entity;

import com.tanaka.template.dto.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String productType;

    private Integer quantity;

    private String farmerEmail;

    private String buyerName; // Optional, who purchased

    @Enumerated(EnumType.STRING)
    private OrderStatus status; // PENDING or COLLECTED

    private LocalDateTime orderTime = LocalDateTime.now();
}

