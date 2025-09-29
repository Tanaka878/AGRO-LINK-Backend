package com.tanaka.template.repository;

import com.tanaka.template.entity.Farmer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FarmerRepository extends JpaRepository<Farmer, Long> {
    Optional<Farmer> findByEmail(String email);
    boolean existsByEmail(String email);
    Optional<Farmer> findById(Long id);
}
