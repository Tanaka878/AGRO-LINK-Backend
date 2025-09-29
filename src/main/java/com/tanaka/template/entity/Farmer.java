package com.tanaka.template.entity;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "farmers")
public class Farmer extends User {

    private String farmName;
    private String farmLocation;
    private Double farmSize; // in hectares or acres
    private String farmingMethods; // e.g., Organic, Conventional
    private String experienceLevel; // e.g., Beginner, Intermediate, Expert

    @ElementCollection
    private List<String> cropsGrown = new ArrayList<>();

    @ElementCollection
    private List<String> livestockOwned = new ArrayList<>();

    @ElementCollection
    private List<String> equipmentAvailable = new ArrayList<>();

    @ElementCollection
    private List<String> certifications = new ArrayList<>();

    @ElementCollection
    private List<String> marketPreferences = new ArrayList<>();
}
