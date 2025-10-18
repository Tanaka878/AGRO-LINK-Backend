package com.tanaka.template.serviceImpl;

import com.tanaka.template.dto.ListedProductsDTO;
import com.tanaka.template.entity.Comment;
import com.tanaka.template.entity.Farmer;
import com.tanaka.template.entity.ListedProducts;
import com.tanaka.template.repository.FarmerRepository;
import com.tanaka.template.repository.ListedProductsRepository;
import com.tanaka.template.service.ListedProductsService;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ListedProductsServiceImpl implements ListedProductsService {

    private final ListedProductsRepository listedProductsRepository;
    private final FarmerRepository farmerRepository;



    public ListedProductsServiceImpl(ListedProductsRepository listedProductsRepository, FarmerRepository farmerRepository) {
        this.listedProductsRepository = listedProductsRepository;
        this.farmerRepository = farmerRepository;
    }

    public List<ListedProductsDTO> getAllListedProductsWithComments() {
        List<ListedProducts> products = listedProductsRepository.findAll();

        return products.stream().map(product -> {
            ListedProductsDTO dto = new ListedProductsDTO();
            dto.setId(product.getId());
            dto.setProductType(product.getProductType());
            dto.setQuantity(product.getQuantity());
            dto.setFarmerEmail(product.getFarmerEmail());

            // Fetch farmer comments
            Optional<Farmer> farmer = farmerRepository.findByEmail(product.getFarmerEmail());
            if (farmer.isPresent()) {
                List<String> comments = farmer.get().getComments()
                        .stream()
                        .map(Comment::getContent)
                        .collect(Collectors.toList());
                dto.setFarmerComments(comments);
            }

            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public ListedProducts addProduct(ListedProducts product) {
        return listedProductsRepository.save(product);
    }

    @Override
    public List<ListedProductsDTO> getAllProducts() {
        return List.of();
    }

    @Override
    public List<ListedProducts> getProductsByFarmer(String farmerEmail) {
        return listedProductsRepository.findByFarmerEmail(farmerEmail);
    }
}
