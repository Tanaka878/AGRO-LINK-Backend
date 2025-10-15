package com.tanaka.template.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "listed_products")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ListedProducts {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String productType;

    private Integer quantity;

    private String farmerEmail;
}
