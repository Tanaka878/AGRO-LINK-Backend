package com.tanaka.template.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FarmerRegistrationDTO {

    // User fields
    private String fullname;
    private String email;
    private String password;
    private Gender gender;   // your existing enum
    private Role role;       // usually FARMER when registering

    // Farmer-specific fields
    private String farmName;
    private String farmLocation;
    private Double farmSize; // hectares or acres
    private String farmingMethods; // e.g., Organic, Conventional
    private String experienceLevel; // Beginner, Intermediate, Expert
    private List<String> cropsGrown;
    private List<String> livestockOwned;
    private List<String> equipmentAvailable;
    private List<String> certifications;
    private List<String> marketPreferences;
}
