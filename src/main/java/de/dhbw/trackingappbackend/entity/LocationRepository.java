package de.dhbw.trackingappbackend.entity;

import org.springframework.data.mongodb.core.geo.GeoJsonPolygon;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface LocationRepository extends MongoRepository<Location, String> {

    List<Location> findByAppUserIdAndPositionWithin(String appUserId, GeoJsonPolygon p);

    List<Location> findByAppUserId(String appUserId);

    // List<Location> findByAppUserIdAndPositionWithin(String appUserId, Box b);
}
