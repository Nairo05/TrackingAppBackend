package de.dhbw.trackingappbackend.entity;

import de.dhbw.trackingappbackend.entity.location.Location;
import de.dhbw.trackingappbackend.entity.location.TileId;
import org.springframework.data.mongodb.core.geo.GeoJsonPolygon;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocationRepository extends MongoRepository<Location, String> {

    List<Location> findByAppUserIdAndPositionWithin(String appUserId, GeoJsonPolygon p);

    Location findByAppUserIdAndTileId(String appUserId, TileId tileId);

    List<Location> findByAppUserId(String appUserId);
}
