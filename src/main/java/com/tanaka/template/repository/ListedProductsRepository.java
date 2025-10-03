package com.tanaka.template.repository;

import com.tanaka.template.entity.ListedProducts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ListedProductsRepository extends JpaRepository<ListedProducts, Long> {
    // Optional: find all products listed by a specific farmer
    List<ListedProducts> findByFarmerEmail(String farmerEmail);
}
