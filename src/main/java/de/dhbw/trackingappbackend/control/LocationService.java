package de.dhbw.trackingappbackend.control;

import de.dhbw.trackingappbackend.entity.LocationRepository;
import de.dhbw.trackingappbackend.entity.location.Location;
import de.dhbw.trackingappbackend.entity.location.Tile;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.geo.GeoJsonPolygon;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Tag(name = "Location Service")
@Service
@RequiredArgsConstructor
public class LocationService {

    private final CoordinateService coordinateService;

    private final LocationRepository locationRepository;

    public List<Location> getLocations(List<String> locationIds, double latitude, double longitude, byte zoomLevel) {

        return Collections.emptyList(); // TODO implement
    }

    public Location addLocation(String appUserId, double latitude, double longitude) {

        return null; // TODO implement
    }
}
