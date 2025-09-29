package com.tanaka.template.service;

import com.tanaka.template.entity.PendingOrder;
import com.tanaka.template.entity.Farmer;

import java.util.List;

public interface PendingOrderService {
    PendingOrder createOrder(PendingOrder order);
    List<PendingOrder> getPendingOrdersForFarmer(Farmer farmer);
}
