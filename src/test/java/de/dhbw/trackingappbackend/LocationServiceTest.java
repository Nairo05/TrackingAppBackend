package de.dhbw.trackingappbackend;

import de.dhbw.trackingappbackend.control.LocationService;
import de.dhbw.trackingappbackend.entity.location.LocationWrapper;
import de.dhbw.trackingappbackend.entity.location.Tile;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Stream;

@SpringBootTest
public class LocationServiceTest {

    @Autowired
    LocationService locationService;

    @Test
    public void testMergeCornerHorizontallyFirst() {

        List<LocationWrapper> locations = Stream.of(
                new Tile(8587, 5664, (byte) 14),
                new Tile(8588, 5663, (byte) 14),
                new Tile(8588, 5664, (byte) 14))
            .map(LocationWrapper::new)
            .toList();

        LocationWrapper expectedResult = LocationWrapper.builder()
            .xTile(locations.get(0).getXTile())
            .yTile(locations.get(0).getYTile())
            .zoomLevel(locations.get(0).getZoomLevel())
            .posUpperLeft(copy(locations.get(0).getPosUpperLeft()))
            .posLowerLeft(copy(locations.get(0).getPosLowerLeft()))
            .posUpperRight(copy(locations.get(2).getPosUpperRight()))
            .posLowerRight(copy(locations.get(2).getPosLowerRight()))
            .mergedVertically(false)
            .mergedHorizontally(true)
            .build();

        List<LocationWrapper> mergedLocations = locationService.mergeLocations(locations);

        Assertions.assertEquals(2, mergedLocations.size());
        Assertions.assertEquals(expectedResult, mergedLocations.get(0));
        Assertions.assertEquals(locations.get(1), mergedLocations.get(1));
    }

    @Test
    public void testMergeCornerVerticallyFirst() {

        List<LocationWrapper> locations = Stream.of(
                new Tile(8588, 5663, (byte) 14),
                new Tile(8587, 5664, (byte) 14),
                new Tile(8588, 5664, (byte) 14))
            .map(LocationWrapper::new)
            .toList();

        LocationWrapper expectedResult = LocationWrapper.builder()
            .xTile(locations.get(0).getXTile())
            .yTile(locations.get(0).getYTile())
            .zoomLevel(locations.get(0).getZoomLevel())
            .posUpperLeft(copy(locations.get(0).getPosUpperLeft()))
            .posUpperRight(copy(locations.get(0).getPosUpperRight()))
            .posLowerLeft(copy(locations.get(2).getPosLowerLeft()))
            .posLowerRight(copy(locations.get(2).getPosLowerRight()))
            .mergedVertically(true)
            .mergedHorizontally(false)
            .build();

        List<LocationWrapper> mergedLocations = locationService.mergeLocations(locations);

        Assertions.assertEquals(2, mergedLocations.size());
        Assertions.assertEquals(expectedResult, mergedLocations.get(0));
        Assertions.assertEquals(locations.get(1), mergedLocations.get(1));
    }

    @Test
    public void testMergeSinglesThenPolygonHorizontally() {

        List<LocationWrapper> locations = Stream.of(
                new Tile(8586, 5664, (byte) 14),
                new Tile(8589, 5664, (byte) 14),
                new Tile(8588, 5664, (byte) 14),
                new Tile(8587, 5664, (byte) 14))
            .map(LocationWrapper::new)
            .toList();

        LocationWrapper expectedResult = LocationWrapper.builder()
            .xTile(locations.get(0).getXTile())
            .yTile(locations.get(0).getYTile())
            .zoomLevel(locations.get(0).getZoomLevel())
            .posUpperLeft(copy(locations.get(0).getPosUpperLeft()))
            .posLowerLeft(copy(locations.get(0).getPosLowerLeft()))
            .posUpperRight(copy(locations.get(1).getPosUpperRight()))
            .posLowerRight(copy(locations.get(1).getPosLowerRight()))
            .mergedVertically(false)
            .mergedHorizontally(true)
            .build();

        List<LocationWrapper> mergedLocations = locationService.mergeLocations(locations);

        Assertions.assertEquals(1, mergedLocations.size());
        Assertions.assertEquals(expectedResult, mergedLocations.get(0));
    }

    @Test
    public void testMergePolygonThenSingleHorizontally() {

        List<LocationWrapper> locations = Stream.of(
                new Tile(8586, 5664, (byte) 14),
                new Tile(8587, 5664, (byte) 14),
                new Tile(8589, 5664, (byte) 14),
                new Tile(8588, 5664, (byte) 14))
            .map(LocationWrapper::new)
            .toList();

        LocationWrapper expectedResult = LocationWrapper.builder()
            .xTile(locations.get(0).getXTile())
            .yTile(locations.get(0).getYTile())
            .zoomLevel(locations.get(0).getZoomLevel())
            .posUpperLeft(copy(locations.get(0).getPosUpperLeft()))
            .posLowerLeft(copy(locations.get(0).getPosLowerLeft()))
            .posUpperRight(copy(locations.get(2).getPosUpperRight()))
            .posLowerRight(copy(locations.get(2).getPosLowerRight()))
            .mergedVertically(false)
            .mergedHorizontally(true)
            .build();

        List<LocationWrapper> mergedLocations = locationService.mergeLocations(locations);

        Assertions.assertEquals(1, mergedLocations.size());
        Assertions.assertEquals(expectedResult, mergedLocations.get(0));
    }

    @Test
    public void testMergeLocationsHorizontally() {

        List<LocationWrapper> locations = Stream.of(
                new Tile(8587, 5664, (byte) 14),
                new Tile(8589, 5664, (byte) 14),
                new Tile(8588, 5664, (byte) 14))
            .map(LocationWrapper::new)
            .toList();

        LocationWrapper expectedResult = LocationWrapper.builder()
            .xTile(locations.get(0).getXTile())
            .yTile(locations.get(0).getYTile())
            .zoomLevel(locations.get(0).getZoomLevel())
            .posUpperLeft(copy(locations.get(0).getPosUpperLeft()))
            .posLowerLeft(copy(locations.get(0).getPosLowerLeft()))
            .posUpperRight(copy(locations.get(1).getPosUpperRight()))
            .posLowerRight(copy(locations.get(1).getPosLowerRight()))
            .mergedVertically(false)
            .mergedHorizontally(true)
            .build();

        List<LocationWrapper> mergedLocations = locationService.mergeLocations(locations);

        Assertions.assertEquals(1, mergedLocations.size());
        Assertions.assertEquals(expectedResult, mergedLocations.get(0));
    }

    @Test
    public void testMergePolygonsHorizontally() {

        List<LocationWrapper> locations = Stream.of(
                new Tile(8585, 5664, (byte) 14),
                new Tile(8589, 5664, (byte) 14),
                new Tile(8586, 5664, (byte) 14),
                new Tile(8588, 5664, (byte) 14),
                new Tile(8587, 5664, (byte) 14))
            .map(LocationWrapper::new)
            .toList();

        LocationWrapper expectedResult = LocationWrapper.builder()
            .xTile(locations.get(0).getXTile())
            .yTile(locations.get(0).getYTile())
            .zoomLevel(locations.get(0).getZoomLevel())
            .posUpperLeft(copy(locations.get(0).getPosUpperLeft()))
            .posLowerLeft(copy(locations.get(0).getPosLowerLeft()))
            .posUpperRight(copy(locations.get(1).getPosUpperRight()))
            .posLowerRight(copy(locations.get(1).getPosLowerRight()))
            .mergedVertically(false)
            .mergedHorizontally(true)
            .build();

        List<LocationWrapper> mergedLocations = locationService.mergeLocations(locations);

        Assertions.assertEquals(1, mergedLocations.size());
        Assertions.assertEquals(expectedResult, mergedLocations.get(0));
    }

    @Test
    public void testMergeLocationsVertically() {

        List<LocationWrapper> locations = Stream.of(
                new Tile(8587, 5664, (byte) 14),
                new Tile(8587, 5666, (byte) 14),
                new Tile(8587, 5665, (byte) 14))
            .map(LocationWrapper::new)
            .toList();

        LocationWrapper expectedResult = LocationWrapper.builder()
            .xTile(locations.get(0).getXTile())
            .yTile(locations.get(0).getYTile())
            .zoomLevel(locations.get(0).getZoomLevel())
            .posUpperLeft(copy(locations.get(0).getPosUpperLeft()))
            .posUpperRight(copy(locations.get(0).getPosUpperRight()))
            .posLowerLeft(copy(locations.get(1).getPosLowerLeft()))
            .posLowerRight(copy(locations.get(1).getPosLowerRight()))
            .mergedVertically(true)
            .mergedHorizontally(false)
            .build();

        List<LocationWrapper> mergedLocations = locationService.mergeLocations(locations);

        Assertions.assertEquals(1, mergedLocations.size());
        Assertions.assertEquals(expectedResult, mergedLocations.get(0));
    }

    @Test
    public void testMergePolygonsVertically() {

        List<LocationWrapper> locations = Stream.of(
                new Tile(8587, 5663, (byte) 14),
                new Tile(8587, 5667, (byte) 14),
                new Tile(8587, 5664, (byte) 14),
                new Tile(8587, 5666, (byte) 14),
                new Tile(8587, 5665, (byte) 14))
            .map(LocationWrapper::new)
            .toList();

        LocationWrapper expectedResult = LocationWrapper.builder()
            .xTile(locations.get(0).getXTile())
            .yTile(locations.get(0).getYTile())
            .zoomLevel(locations.get(0).getZoomLevel())
            .posUpperLeft(copy(locations.get(0).getPosUpperLeft()))
            .posUpperRight(copy(locations.get(0).getPosUpperRight()))
            .posLowerLeft(copy(locations.get(1).getPosLowerLeft()))
            .posLowerRight(copy(locations.get(1).getPosLowerRight()))
            .mergedVertically(true)
            .mergedHorizontally(false)
            .build();

        List<LocationWrapper> mergedLocations = locationService.mergeLocations(locations);

        Assertions.assertEquals(1, mergedLocations.size());
        Assertions.assertEquals(expectedResult, mergedLocations.get(0));
    }

    private double[] copy(double[] array) {
        double d1 = array[0];
        double d2 = array[1];
        return new double[] {d1, d2};
    }
}
