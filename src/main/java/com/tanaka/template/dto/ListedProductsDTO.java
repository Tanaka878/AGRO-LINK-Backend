package com.tanaka.template.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ListedProductsDTO {

    private Long id;
    private String productType;
    private Integer quantity;
    private String farmerEmail;
    private Double price;

    private List<String> farmerComments;
}
