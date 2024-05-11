package de.dhbw.trackingappbackend.control;

import de.dhbw.trackingappbackend.entity.location.Tile;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.geo.GeoJsonPolygon;
import org.springframework.stereotype.Service;

@Tag(name = "Coordinate Service")
@Service
@RequiredArgsConstructor
public class CoordinateService {

    /**
     * Returns an area as a GeoJsonPolygon using the given latitude, longitude and zoom level.
     *
     * @param latitude given latitude
     * @param longitude given longitude
     * @param zoomLevel given zoom level
     * @return area as GeoJsonPolygon
     */
    public GeoJsonPolygon getGeoJsonPolygon(double latitude, double longitude, byte zoomLevel) {

        double tileOffset = calculateDistance(latitude, longitude, zoomLevel);
        int zoomFactor = 15 - zoomLevel;
        int latLength = 12;
        int lonLength = 24;

        double minLat = 45.08903556483102;
        double maxLat = 55.7765730186677;
        double minLong = 5.625;
        double maxLong = 16.875;

        // TODO adjust to what frontend sees, idk how this behaves with lower zoom levels
        double latOffset = tileOffset * (latLength / 2.0) * zoomFactor;
        double lonOffset = tileOffset * (lonLength / 2.0) * zoomFactor;

        GeoJsonPolygon polygon = new GeoJsonPolygon(
            new Point(Math.max(latitude - latOffset, minLat), Math.max(longitude - lonOffset, minLong)), // upper left
            new Point(Math.max(latitude - latOffset, minLat), Math.min(longitude + lonOffset, maxLong)), // upper right
            new Point(Math.min(latitude + latOffset, maxLat), Math.min(longitude + lonOffset, maxLong)), // lower right
            new Point(Math.min(latitude + latOffset, maxLat), Math.max(longitude - lonOffset, minLong)), // lower left
            new Point(Math.max(latitude - latOffset, minLat), Math.max(longitude - lonOffset, minLong))); // first and last point have to be the same

        return polygon;
    }

    /**
     * Calculates the approximate length of a tile based on given latitude and longitude.
     * Different to the latitude, the distance between longitudes varies due to the earth's shape.
     *
     * @param latitude given latitude
     * @param longitude given longitude
     * @param zoomLevel given zoom level
     * @return horizontal distance of a tile in m
     */
    public double calculateDistance(double latitude, double longitude, byte zoomLevel) {

        Tile tile = TileService.getTileByCoordinates(latitude, longitude, zoomLevel);
        double[] pos1 = TileService.getCoordinatesByTile(tile.getXTile(), tile.getYTile(), zoomLevel);
        double[] pos2 = TileService.getCoordinatesByTile(tile.getXTile() + 1, tile.getYTile(), zoomLevel);

        return pos2[1] - pos1[1];
    }
}
