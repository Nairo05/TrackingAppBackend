package de.dhbw.trackingappbackend.control;

import de.dhbw.trackingappbackend.entity.LocationRepository;
import de.dhbw.trackingappbackend.entity.location.Location;
import de.dhbw.trackingappbackend.entity.location.Tile;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.geo.GeoJsonPolygon;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Tag(name = "Location Service")
@Service
@RequiredArgsConstructor
public class LocationService {

    private final CoordinateService coordinateService;

    private final LocationRepository locationRepository;

    /**
     * Returns a list of visited location of a user by given zoomLevel, with given lat/lon coordinates as the center anchor point.
     *
     * @param appUserId id of the user
     * @param latitude latitude of center anchor point
     * @param longitude longitude of center anchor point
     * @param zoomLevel zoom level of the map
     * @return list of visited locations
     */
    public List<Location> getLocations(String appUserId, double latitude, double longitude, byte zoomLevel) {

        GeoJsonPolygon polygon = coordinateService.getGeoJsonPolygon(latitude, longitude, zoomLevel);
        return locationRepository.findByAppUserIdAndTilePositionWithin(appUserId, polygon);
    }

    public Location addLocation(String appUserId, double latitude, double longitude) {

        Tile tile = TileService.getTileByCoordinates(latitude, longitude, (byte) 14);

        if (locationRepository.existsByAppUserIdAndTile(appUserId, tile)) {
            return null; // location already visited
        }

        // TODO change data structure to static set of locations, no need for appUserId and generating UUID
        Location newLocation = new Location(UUID.randomUUID().toString(), tile, appUserId);
        locationRepository.save(newLocation);

        return newLocation;
    }
}
