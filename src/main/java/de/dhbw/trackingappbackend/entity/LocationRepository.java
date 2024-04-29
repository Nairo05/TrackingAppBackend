package de.dhbw.trackingappbackend.entity;

import de.dhbw.trackingappbackend.entity.location.Location;
import de.dhbw.trackingappbackend.entity.location.Tile;
import org.springframework.data.mongodb.core.geo.GeoJsonPolygon;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocationRepository extends MongoRepository<Location, String> {

    List<Location> findByAppUserIdAndTilePositionWithin(String appUserId, GeoJsonPolygon p);

    List<Location> findByTilePositionWithin(GeoJsonPolygon p);

    Location findByAppUserIdAndTile(String appUserId, Tile tile);

    boolean existsByAppUserIdAndTile(String appUserId, Tile tile);

    List<Location> findByAppUserId(String appUserId);
}
