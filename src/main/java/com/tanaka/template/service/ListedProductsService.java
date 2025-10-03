package com.tanaka.template.service;

import com.tanaka.template.entity.ListedProducts;

import java.util.List;

public interface ListedProductsService {

    // Create or add a new listed product
    ListedProducts addProduct(ListedProducts product);

    // Get all listed products
    List<ListedProducts> getAllProducts();

    // Get products by a specific farmer
    List<ListedProducts> getProductsByFarmer(String farmerEmail);
}
