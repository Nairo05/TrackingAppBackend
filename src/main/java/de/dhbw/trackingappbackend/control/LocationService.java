package de.dhbw.trackingappbackend.control;

import de.dhbw.trackingappbackend.entity.LocationRepository;
import de.dhbw.trackingappbackend.entity.location.Location;
import de.dhbw.trackingappbackend.entity.location.LocationWrapper;
import de.dhbw.trackingappbackend.entity.location.Tile;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.geo.GeoJsonPolygon;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Tag(name = "Location Service")
@Service
@RequiredArgsConstructor
public class LocationService {

    private final CoordinateService coordinateService;

    private final LocationRepository locationRepository;

    /**
     * Returns a list of location wrappers of a user by given zoomLevel, starting from the given lat/lon coordinates as the west/south anchor point.
     * Adds unvisited locations to the list, that have an oppacity of 127 (not see-through
     *
     * @param appUserId id of the user
     * @param latitude latitude of the anchor point
     * @param longitude longitude of the anchor point
     * @param zoomLevel zoom level of the map
     * @return list of location wrappers
     */
    public List<LocationWrapper> getLocations(String appUserId, double latitude, double longitude, byte zoomLevel) {

        GeoJsonPolygon polygon = coordinateService.getGeoJsonPolygon(latitude, longitude, zoomLevel);
        List<Location> visitedLocations = locationRepository.findByAppUserIdAndTilePositionWithin(appUserId, polygon);

        Tile initialTile = TileService.getTileByCoordinates(latitude, longitude, zoomLevel);
        List<Tile> visitedTiles = visitedLocations.stream()
            .map(Location::getTile)
            .toList();

        // TODO refactor: change to db query instead of calculation
        //      method changes when different zoom levels are used
        List<Tile> unvisitedTiles = IntStream
            .range(initialTile.getXTile() - 5, initialTile.getXTile() + 5 + 1)
            .mapToObj(x ->
                    IntStream.range(initialTile.getYTile() - 10, initialTile.getYTile() + 10 + 1)
                        .filter(y -> !visitedTiles.contains(new Tile(x, y, zoomLevel)))
                        .mapToObj(y -> new Tile(x, y, zoomLevel)))
            .flatMap(Function.identity())
            .toList();

        List<LocationWrapper> unvisitedLocationWrappers = unvisitedTiles.stream()
            .map(LocationWrapper::new)
            .toList();

        List<LocationWrapper> visitedLocationWrappers = visitedLocations.stream()
            .map(LocationWrapper::new)
            .toList();

        return Stream
            .concat(visitedLocationWrappers.stream(), unvisitedLocationWrappers.stream())
            .toList();
    }
}
