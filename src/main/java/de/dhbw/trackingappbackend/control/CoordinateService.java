package de.dhbw.trackingappbackend.control;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.geo.GeoJsonPolygon;
import org.springframework.stereotype.Service;

@Tag(name = "Coordinate Service")
@Service
@RequiredArgsConstructor
public class CoordinateService {

    private static final double EARTH_CIRCUMFERENCE = 40075016.686; // in m

    /**
     * Returns an area as a GeoJsonPolygon using the given latitude, longitude and zoom level.
     *
     * @param latitude given latitude
     * @param longitude given longitude
     * @param zoomLevel given zoom level
     * @return area as GeoJsonPolygon
     */
    public GeoJsonPolygon getGeoJsonPolygon(double latitude, double longitude, byte zoomLevel) {

        double tileOffset = calculateDistance(latitude, zoomLevel);
        int xOffset = 6;
        int yOffset = 12;

        // TODO messy, refactor! Es steht noch nicht fest, wie viele Tiles vom FE bei welchem Zoomlevel benötigt werden
        //  daher wird vorerst nur ein fest definierter Bereich zurückgegeben (grob 8 Tiles horizontal, 16 Tiles vertikal)
        //  Außerdem wird die variierende Distanz zwischen Längengraden noch nicht berücksichtigt!
        return new GeoJsonPolygon(
            new Point(latitude, longitude),
            new Point(latitude, longitude + tileOffset * yOffset),
            new Point(latitude + tileOffset * xOffset, longitude + tileOffset * yOffset),
            new Point(latitude + tileOffset * xOffset, longitude),
            new Point(latitude, longitude)); // first and last point have to be the same
    }

    /**
     * Calculates the horizontal distance of a tile based on the given latitude and zoom level.
     * Different to the latitude, the distance between longitudes varies due to the earth's shape.
     * Latitude: -, Longitude: |
     *
     * @link <a href="https://wiki.openstreetmap.org/wiki/Zoom_levels">Calculation Method</a>
     *
     * @param latitude given latitude
     * @param zoomLevel given zoom level
     * @return horizontal distance of a tile in m
     */
    public double calculateDistance(double latitude, byte zoomLevel) {
        return EARTH_CIRCUMFERENCE * Math.cos(Math.toRadians(latitude)) / Math.pow(2, zoomLevel + 8); // TODO test this
    }
}
