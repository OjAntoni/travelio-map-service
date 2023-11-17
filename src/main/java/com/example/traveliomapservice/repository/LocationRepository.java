package com.example.traveliomapservice.repository;

import com.example.traveliomapservice.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<Location, Long> {
}
