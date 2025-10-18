package com.tanaka.template.service;

import com.tanaka.template.dto.CommentDTO;
import com.tanaka.template.dto.FarmerRegistrationDTO;
import com.tanaka.template.dto.FarmerStatistics;
import com.tanaka.template.entity.Comment;
import com.tanaka.template.entity.Farmer;
import org.springframework.http.ResponseEntity;

public interface FarmerService {
    Farmer registerFarmer(FarmerRegistrationDTO dto);
    Farmer getFarmerByEmail(String email);

    ResponseEntity<FarmerStatistics> getStatistics(String email);
    public ResponseEntity<String> addCommentForFarmer(String farmerEmail, CommentDTO dto);

}
