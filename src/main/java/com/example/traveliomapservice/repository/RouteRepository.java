package com.example.traveliomapservice.repository;

import com.example.traveliomapservice.model.Route;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RouteRepository extends JpaRepository<Route, Long> {
    boolean existsById(long id);
}
