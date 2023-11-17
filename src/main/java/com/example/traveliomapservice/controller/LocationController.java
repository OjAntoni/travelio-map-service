package com.example.traveliomapservice.controller;

import com.example.traveliomapservice.dto.LocationDto;
import com.example.traveliomapservice.exception.EntityNotFoundException;
import com.example.traveliomapservice.model.Location;
import com.example.traveliomapservice.service.MapService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/location")
@AllArgsConstructor
public class LocationController {
    private MapService mapService;
    private ObjectMapper objectMapper;

    /**
     *
     * @param json
     * {
     *   "point": "POINT (latitude longitude)"
     * }
     * @return LocationDto
     */
    @PostMapping
    public ResponseEntity<?> createLocation(@RequestBody String json){
        WKTReader reader = new WKTReader();
        Point p;
        try {
            String pointWKT = objectMapper.readTree(json).get("point").textValue();
            p = (Point) reader.read(pointWKT);
        } catch (ParseException | JsonProcessingException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(mapService.create(p), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getLocation(@PathVariable long id){
        try {
            LocationDto location = mapService.get(id);
            return new ResponseEntity<>(location, HttpStatus.OK);
        } catch (EntityNotFoundException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateLocation(@PathVariable long id, @RequestBody String json){
        WKTReader reader = new WKTReader();
        Point p;
        try {
            String pointWKT = objectMapper.readTree(json).get("point").textValue();
            p = (Point) reader.read(pointWKT);
            return new ResponseEntity<>(mapService.update(new Location(id, p)), HttpStatus.OK);
        } catch (ParseException | JsonProcessingException | EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteLocation(@PathVariable long id){
        mapService.deleteLocation(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
