package de.dhbw.trackingappbackend.entity;

import de.dhbw.trackingappbackend.entity.location.Location;
import org.springframework.data.mongodb.core.geo.GeoJsonPolygon;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocationRepository extends MongoRepository<Location, String> {

    List<Location> findByAppUserIdAndPositionWithin(String appUserId, GeoJsonPolygon p);

    List<Location> findByAppUserId(String appUserId);

    // List<Location> findByAppUserIdAndPositionWithin(String appUserId, Box b);
}
