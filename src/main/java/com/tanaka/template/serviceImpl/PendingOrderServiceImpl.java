package com.tanaka.template.serviceImpl;

import com.tanaka.template.entity.PendingOrder;
import com.tanaka.template.entity.Farmer;
import com.tanaka.template.repository.PendingOrderRepository;
import com.tanaka.template.service.PendingOrderService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PendingOrderServiceImpl implements PendingOrderService {

    private final PendingOrderRepository pendingOrderRepository;

    public PendingOrderServiceImpl(PendingOrderRepository pendingOrderRepository) {
        this.pendingOrderRepository = pendingOrderRepository;
    }

    @Override
    public PendingOrder createOrder(PendingOrder order) {
        return pendingOrderRepository.save(order);
    }

    @Override
    public List<PendingOrder> getPendingOrdersForFarmer(Farmer farmer) {
        return pendingOrderRepository.findByFarmer(farmer);
    }
}
