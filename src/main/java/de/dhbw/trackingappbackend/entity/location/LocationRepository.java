package de.dhbw.trackingappbackend.entity.location;

import org.springframework.data.mongodb.core.geo.GeoJsonPolygon;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LocationRepository extends MongoRepository<Location, String> {

    List<Location> findByTilePositionWithinAndIdIn(GeoJsonPolygon p, List<String> locationIds);

    List<Location> findByIdIn(List<String> locationIds);

    Optional<Location> findByTile_xTileAndTile_yTile(int xTile, int yTile);

    int countAllByIdInAndKuerzelContains(List<String> locationIds, String kuerzel);
}
