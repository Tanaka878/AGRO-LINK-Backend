package com.tanaka.template.service;

import com.tanaka.template.dto.FarmerRegistrationDTO;
import com.tanaka.template.entity.Farmer;

public interface FarmerService {
    Farmer registerFarmer(FarmerRegistrationDTO dto);
    Farmer getFarmerByEmail(String email);
}
