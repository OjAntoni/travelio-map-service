package com.example.traveliomapservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RouteDto {
    private Long id;
    private List<CoordinatePair> path;

    @AllArgsConstructor
    @Data
    public static class CoordinatePair {
        private double latitude;
        private double longitude;
    }
}
