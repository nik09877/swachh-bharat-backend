package com.nikhil.swachhbharatbackend.repositories;

import com.nikhil.swachhbharatbackend.models.PickupLocation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PickupLocationRepository extends JpaRepository<PickupLocation, Long> {
    List<PickupLocation> findByUserId(Long userId);
    List<PickupLocation> findByCityIn(String[] cities);
}
