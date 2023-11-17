package com.example.traveliomapservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class LocationDto {
    public long id;
    public double latitude;
    public double longitude;
}
