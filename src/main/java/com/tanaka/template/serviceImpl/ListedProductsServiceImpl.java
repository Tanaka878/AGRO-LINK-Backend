package com.tanaka.template.serviceImpl;

import com.tanaka.template.entity.ListedProducts;
import com.tanaka.template.repository.ListedProductsRepository;
import com.tanaka.template.service.ListedProductsService;
import com.tanaka.template.util.MailSenderService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListedProductsServiceImpl implements ListedProductsService {

    private final ListedProductsRepository listedProductsRepository;
    private final MailSenderService mailSenderService;

    public ListedProductsServiceImpl(ListedProductsRepository listedProductsRepository, MailSenderService mailSenderService) {
        this.listedProductsRepository = listedProductsRepository;
        this.mailSenderService = mailSenderService;
    }

    @Override
    @Transactional
    public ListedProducts addProduct(ListedProducts product) {
        mailSenderService.sendListingEmailToFarmer(product);

        System.out.println("****** adding product to repository ****");
        System.out.println(product);
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
