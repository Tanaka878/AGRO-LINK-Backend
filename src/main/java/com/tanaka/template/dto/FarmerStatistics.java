package com.tanaka.template.dto;

import lombok.Data;

@Data
public class FarmerStatistics {
    private long pendingOrders;
    private long completedOrders;
    private long cancelledOrders;
    private int listedProducts;

}
