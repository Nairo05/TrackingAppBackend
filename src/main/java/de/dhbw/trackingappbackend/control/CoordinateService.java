package de.dhbw.trackingappbackend.control;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.geo.GeoJsonPolygon;
import org.springframework.stereotype.Service;

@Tag(name = "Coordinate Service")
@Service
@RequiredArgsConstructor
public class CoordinateService {

    /*public GeoJsonPolygon getGeoJsonPolygon(double latitude, double longitude, byte zoomLevel) {

        return null;
    }*/

    public GeoJsonPolygon getGeoJsonPolygon(double lon1, double lat1, double lon2, double lat2) {

        GeoJsonPolygon polygon = new GeoJsonPolygon(
            new org.springframework.data.geo.Point(lon1, lat1),
            new org.springframework.data.geo.Point(lon1, lat2),
            new org.springframework.data.geo.Point(lon2, lat2),
            new org.springframework.data.geo.Point(lon2, lat1),
            new org.springframework.data.geo.Point(lon1, lat1)); // first and last point have to be the same

        return polygon;
    }

    public double calculateDistance(double lat1, double lon1, double lat2, double lon2) {

        double theta = lon1 - lon2;
        double dist = Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2)) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(theta));

        dist = Math.acos(dist);
        dist = Math.toDegrees(dist);
        dist = dist * 60 * 1.1515;
        dist = dist * 1.609344;

        return (dist);
    }
}
