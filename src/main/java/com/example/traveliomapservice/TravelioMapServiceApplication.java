package com.example.traveliomapservice;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.geo.Point;

@SpringBootApplication
public class TravelioMapServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(TravelioMapServiceApplication.class, args);
    }

}
