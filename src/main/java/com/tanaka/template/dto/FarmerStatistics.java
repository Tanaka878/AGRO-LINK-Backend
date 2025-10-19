package com.tanaka.template.dto;

import lombok.Data;

import java.util.Map;

@Data
public class FarmerStatistics {
    private long pendingOrders;
    private long completedOrders;
    private long cancelledOrders;
    private int listedProducts;

    // Map of productType -> totalQuantity
    private Map<String, Integer> topSellingProducts;
}
