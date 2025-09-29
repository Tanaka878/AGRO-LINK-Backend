package com.tanaka.template.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "pending_orders")
public class PendingOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String productType;       // e.g., Maize, Beans
    private Integer quantity;         // quantity ordered
    private LocalDateTime orderPlaced; // timestamp when order was placed

    @ManyToOne
    @JoinColumn(name = "farmer_id")
    private Farmer farmer;           // link order to a farmer
}
