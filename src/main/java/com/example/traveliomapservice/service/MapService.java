package com.example.traveliomapservice.service;

import com.example.traveliomapservice.dto.LocationDto;
import com.example.traveliomapservice.dto.RouteDto;
import com.example.traveliomapservice.exception.EntityNotFoundException;
import com.example.traveliomapservice.model.Location;
import com.example.traveliomapservice.model.Route;
import com.example.traveliomapservice.repository.LocationRepository;
import com.example.traveliomapservice.repository.RouteRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.AllArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class MapService {
    LocationRepository locationRepository;
    RouteRepository routeRepository;
    @PersistenceContext
    private EntityManager entityManager;

    //CRUD location
    @Transactional
    public LocationDto create(Point p) {
        Location location = locationRepository.save(new Location(reverseCoordinates(p)));
        return LocationDto.builder()
                .id(location.getId())
                .latitude(location.getCoordinates().getX())
                .longitude(location.getCoordinates().getY())
                .build();
    }

    private Point reverseCoordinates(Point originalPoint) {
        GeometryFactory geometryFactory = new GeometryFactory();
    Coordinate originalCoordinate = originalPoint.getCoordinate();
    Coordinate newCoordinate = new Coordinate(originalCoordinate.y, originalCoordinate.x);
        return geometryFactory.createPoint(newCoordinate);
}

    public LocationDto get(long id) throws EntityNotFoundException {
        Location location = locationRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        return LocationDto.builder()
                .id(location.getId())
                .latitude(location.getCoordinates().getX())
                .longitude(location.getCoordinates().getY())
                .build();
    }

    @Transactional
    public LocationDto update(Location location) throws EntityNotFoundException {
        Location oldLocation = locationRepository.findById(location.getId()).orElseThrow(EntityNotFoundException::new);
        oldLocation.setCoordinates(location.getCoordinates());
        return LocationDto.builder()
                .id(oldLocation.getId())
                .latitude(oldLocation.getCoordinates().getX())
                .longitude(oldLocation.getCoordinates().getY())
                .build();
    }

    @Transactional
    public void deleteLocation(long id) {
        locationRepository.deleteById(id);
    }

    //CRUD - route

    @Transactional
    public RouteDto create(LineString lineString){
        Route route = new Route(lineString);
        reverseCoordInRoute(route);
        Route saved = routeRepository.save(route);
        return convertToDTO(saved);
    }

    private void reverseCoordInRoute(Route route){
        for (Coordinate c : route.getRouteLine().getCoordinates()) {
            double tmp = c.getX();
            c.setX(c.getY());
            c.setY(tmp);
        }
    }

    private RouteDto convertToDTO(Route route) {
        RouteDto dto = new RouteDto();
        dto.setId(route.getId());

        List<RouteDto.CoordinatePair> path = new ArrayList<>();
        for (Coordinate coord : route.getRouteLine().getCoordinates()) {
            path.add(new RouteDto.CoordinatePair(coord.getY(), coord.getX())); // latitude, longitude
        }
        dto.setPath(path);

        return dto;
    }

    private Route convertToRoute(RouteDto routeDto){
        GeometryFactory geometryFactory = new GeometryFactory();
        Route route = new Route();
        route.setId(routeDto.getId());

        if (routeDto.getPath() != null && !routeDto.getPath().isEmpty()) {
            Coordinate[] coordinates = routeDto.getPath().stream()
                    .map(cp -> new Coordinate(cp.getLongitude(), cp.getLatitude()))
                    .toArray(Coordinate[]::new);
            LineString lineString = geometryFactory.createLineString(coordinates);
            route.setRouteLine(lineString);
        }
        return route;
    }

    public RouteDto getRoute(long id) throws EntityNotFoundException {
        Route route = routeRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        reverseCoordInRoute(route);
        return convertToDTO(route);
    }

    public RouteDto update(RouteDto dto) throws EntityNotFoundException {
        if(routeRepository.existsById(dto.getId())){
            Route route = convertToRoute(dto);
            Route saved = routeRepository.save(route);
            entityManager.detach(saved);
            return convertToDTO(saved);
        } else {
            throw new EntityNotFoundException();
        }
    }

    public void deleteRoute(long id){
        routeRepository.deleteById(id);
    }
}
