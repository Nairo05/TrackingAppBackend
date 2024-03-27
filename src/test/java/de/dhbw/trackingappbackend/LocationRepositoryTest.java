package de.dhbw.trackingappbackend;

import de.dhbw.trackingappbackend.control.CoordinateService;
import de.dhbw.trackingappbackend.entity.Location;
import de.dhbw.trackingappbackend.entity.LocationRepository;
import de.dhbw.trackingappbackend.entity.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class LocationRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    LocationRepository locationRepository;

    @Autowired
    CoordinateService coordinateService;

    @Test
    public void testFindLocationsByUser() {

        String appUserId = userRepository.findByEmail("test1@test.de").get().getId();
        List<Location> locationList = locationRepository.findByAppUserId(appUserId);

        assert(locationList.size() == 4);
    }

    @Test
    public void testFindLocationsWithinGeoJsonPolygon() {

        String appUserId = userRepository.findByEmail("test1@test.de").get().getId();

        List<Location> locationList1 = locationRepository.findByAppUserIdAndPositionWithin(appUserId,
            coordinateService.getGeoJsonPolygon(0, 0, 1, 1));
        List<Location> locationList2 = locationRepository.findByAppUserIdAndPositionWithin(appUserId,
            coordinateService.getGeoJsonPolygon(0, 0, 1, 0.5));
        List<Location> locationList3 = locationRepository.findByAppUserIdAndPositionWithin(appUserId,
            coordinateService.getGeoJsonPolygon(0, 0, 0.5, 0.5));

        assert(locationList1.size() == 4);
        assert(locationList2.size() == 3);
        assert(locationList3.size() == 2); // points on the border are not included
    }

    /*
     * Legacy queries with box/polygon not supported by Azure Cosmos
     *
    @Test
    public void testFindLocationsWithinBox() {

        String appUserId = userRepository.findByEmail("test1@test.de").get().getId();

        List<Location> locationList1 = locationRepository.findByAppUserIdAndPositionWithin(appUserId,
            new Box(new Point(0, 0), new Point(1, 1)));
        List<Location> locationList2 = locationRepository.findByAppUserIdAndPositionWithin(appUserId,
            new Box(new Point(0, 0), new Point(1, 0.5)));
        List<Location> locationList3 = locationRepository.findByAppUserIdAndPositionWithin(appUserId,
            new Box(new Point(0, 0), new Point(0.5, 0.5)));

        assert(locationList1.size() == 4);
        assert(locationList2.size() == 3);
        assert(locationList3.size() == 2);
    }*/
}
