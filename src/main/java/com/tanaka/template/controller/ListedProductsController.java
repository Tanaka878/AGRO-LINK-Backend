package com.tanaka.template.controller;

import com.tanaka.template.dto.ListedProductsDTO;
import com.tanaka.template.entity.ListedProducts;
import com.tanaka.template.service.ListedProductsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/listed-products")
public class ListedProductsController {

    private final ListedProductsService listedProductsService;

    public ListedProductsController(ListedProductsService listedProductsService) {
        this.listedProductsService = listedProductsService;
    }

    // Add a new listed product
    @PostMapping("/add")
    public ResponseEntity<ListedProducts> addProduct(@RequestBody ListedProducts product) {
        ListedProducts savedProduct = listedProductsService.addProduct(product);
        return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
    }

    // Get all listed products
    @GetMapping("/all")
    public ResponseEntity<List<ListedProductsDTO>> getAllProducts() {
        List<ListedProductsDTO> products = listedProductsService.getAllListedProductsWithComments();
        return ResponseEntity.ok(products);
    }

    // Get all products by a specific farmer
    @GetMapping("/farmer/{email}")
    public ResponseEntity<List<ListedProducts>> getProductsByFarmer(@PathVariable String email) {
        List<ListedProducts> products = listedProductsService.getProductsByFarmer(email);
        return ResponseEntity.ok(products);
    }

    // Delete a product by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        listedProductsService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
