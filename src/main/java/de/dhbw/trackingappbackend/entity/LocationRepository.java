package de.dhbw.trackingappbackend.entity;

import de.dhbw.trackingappbackend.entity.location.Location;
import de.dhbw.trackingappbackend.entity.location.Tile;
import org.springframework.data.mongodb.core.geo.GeoJsonPolygon;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LocationRepository extends MongoRepository<Location, String> {

    // List<Location> findByTilePositionWithin(GeoJsonPolygon p);

    List<Location> findByTilePositionWithinAndIdIn(GeoJsonPolygon p, List<String> locationIds);

    List<Location> findByIdIn(List<String> locationIds);

    Optional<Location> findByTile(Tile tile);

    int countLocationsByIdInAndKuerzelContains(List<String> locationIds, String kuerzel);
}
