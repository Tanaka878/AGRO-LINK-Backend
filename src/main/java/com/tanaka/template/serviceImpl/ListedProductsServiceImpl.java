package com.tanaka.template.serviceImpl;

import com.tanaka.template.entity.ListedProducts;
import com.tanaka.template.repository.ListedProductsRepository;
import com.tanaka.template.service.ListedProductsService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListedProductsServiceImpl implements ListedProductsService {

    private final ListedProductsRepository listedProductsRepository;

    public ListedProductsServiceImpl(ListedProductsRepository listedProductsRepository) {
        this.listedProductsRepository = listedProductsRepository;
    }

    @Override
    public ListedProducts addProduct(ListedProducts product) {
        return listedProductsRepository.save(product);
    }

    @Override
    public List<ListedProducts> getAllProducts() {
        return listedProductsRepository.findAll();
    }

    @Override
    public List<ListedProducts> getProductsByFarmer(String farmerEmail) {
        return listedProductsRepository.findByFarmerEmail(farmerEmail);
    }
}
