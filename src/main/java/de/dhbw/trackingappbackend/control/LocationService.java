package de.dhbw.trackingappbackend.control;

import de.dhbw.trackingappbackend.entity.location.Location;
import de.dhbw.trackingappbackend.entity.location.LocationWrapper;
import de.dhbw.trackingappbackend.entity.location.Tile;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Tag(name = "Location Service")
@Service
@RequiredArgsConstructor
public class LocationService {

    public List<LocationWrapper> mergeLocations(List<LocationWrapper> locations) {

        List<LocationWrapper> locationsPolygons = new ArrayList<>();

        for (LocationWrapper single : locations) {
            LocationWrapper lastMergedPoly = null;
            LocationWrapper toRemove = null;
            for (LocationWrapper polygon : locationsPolygons) {

                // check if single location can be merged vertically
                if (Arrays.equals(single.getPosUpperLeft(), polygon.getPosLowerLeft()) &&
                        Arrays.equals(single.getPosUpperRight(), polygon.getPosLowerRight()) &&
                        !polygon.isMergedHorizontally() && !single.isMergedHorizontally()) {
                    // single location is below polygon
                    polygon.setPosLowerLeft(single.getPosLowerLeft());
                    polygon.setPosLowerRight(single.getPosLowerRight());

                    single.setMergedVertically(true);
                    polygon.setMergedVertically(true);
                    lastMergedPoly = polygon;
                }
                if (Arrays.equals(single.getPosLowerLeft(), polygon.getPosUpperLeft()) &&
                        Arrays.equals(single.getPosLowerRight(), polygon.getPosUpperRight()) &&
                        !polygon.isMergedHorizontally() && !single.isMergedHorizontally()) {
                    // single location is above polygon
                    if (single.isMergedVertically()) {
                        // single location is between two polygons
                        lastMergedPoly.setPosLowerLeft(polygon.getPosLowerLeft());
                        lastMergedPoly.setPosLowerRight(polygon.getPosLowerRight());
                        toRemove = polygon;
                        continue;
                    } else {
                        polygon.setPosUpperLeft(single.getPosUpperLeft());
                        polygon.setPosUpperRight(single.getPosUpperRight());
                    }
                    polygon.setPosUpperLeft(single.getPosUpperLeft());
                    polygon.setPosUpperRight(single.getPosUpperRight());

                    single.setMergedVertically(true);
                    polygon.setMergedVertically(true);
                }

                // check if single location can be merged horizontally
                if (Arrays.equals(single.getPosUpperLeft(), polygon.getPosUpperRight()) &&
                        Arrays.equals(single.getPosLowerLeft(), polygon.getPosLowerRight()) &&
                        !polygon.isMergedVertically() && !single.isMergedVertically()) {
                    // single location is on the right of polygon
                    polygon.setPosUpperRight(single.getPosUpperRight());
                    polygon.setPosLowerRight(single.getPosLowerRight());

                    single.setMergedHorizontally(true);
                    polygon.setMergedHorizontally(true);
                    lastMergedPoly = polygon;
                }
                if (Arrays.equals(single.getPosUpperRight(), polygon.getPosUpperLeft()) &&
                        Arrays.equals(single.getPosLowerRight(), polygon.getPosLowerLeft()) &&
                        !polygon.isMergedVertically() && !single.isMergedVertically()) {
                    // single location is on the left of polygon
                    if (single.isMergedHorizontally()) {
                        // single location is between two polygons
                        lastMergedPoly.setPosUpperRight(polygon.getPosUpperRight());
                        lastMergedPoly.setPosLowerRight(polygon.getPosLowerRight());
                        toRemove = polygon;
                    }
                    else {
                        polygon.setPosUpperLeft(single.getPosUpperLeft());
                        polygon.setPosLowerLeft(single.getPosLowerLeft());
                    }

                    single.setMergedHorizontally(true);
                    polygon.setMergedHorizontally(true);
                }
            }
            // single location is in no polygon, add separately
            if (!single.isMergedVertically() && !single.isMergedHorizontally()) {
                locationsPolygons.add(single);
            }
            if (toRemove != null) locationsPolygons.remove(toRemove);
        }

        return locationsPolygons;
    }

    public List<LocationWrapper> zoomLocations(List<Location> locations, byte zoomLevel) {

        if (zoomLevel == 14) return locations.stream()
                .map(LocationWrapper::new)
                .toList();

        Set<Tile> parentTiles = new HashSet<>();

        locations.forEach(location -> parentTiles.add(TileService.getParentTile(location.getTile(), zoomLevel)));

        return parentTiles.stream()
                .map(LocationWrapper::new)
                .toList();
    }
}
