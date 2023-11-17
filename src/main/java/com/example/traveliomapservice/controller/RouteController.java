package com.example.traveliomapservice.controller;

import com.example.traveliomapservice.dto.RouteDto;
import com.example.traveliomapservice.exception.EntityNotFoundException;
import com.example.traveliomapservice.model.Route;
import com.example.traveliomapservice.service.MapService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/route")
@AllArgsConstructor
public class RouteController {
    private MapService mapService;
    private ObjectMapper objectMapper;

    /**
     * {
     *   "routeLine": "LINESTRING (latitude longitude, latitude longitude, ...)"
     * }
     * @return RouteDto
     */
    @PostMapping
    public ResponseEntity<?> createRoute(@RequestBody String json){
        WKTReader reader = new WKTReader();
        LineString r;
        try {
            String routeWKT = objectMapper.readTree(json).get("routeLine").textValue();
            r = (LineString) reader.read(routeWKT);
        } catch (ParseException | JsonProcessingException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(mapService.create(r), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getRoute(@PathVariable long id){
        try {
            return new ResponseEntity<>(mapService.getRoute(id), HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping
    public ResponseEntity<?> updateRoute(@RequestBody RouteDto dto){
        try {
            return new ResponseEntity<>(mapService.update(dto), HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRoute(@PathVariable long id){
        mapService.deleteRoute(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
