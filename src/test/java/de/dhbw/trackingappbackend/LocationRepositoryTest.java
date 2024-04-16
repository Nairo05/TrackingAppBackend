package de.dhbw.trackingappbackend;

import de.dhbw.trackingappbackend.control.CoordinateService;
import de.dhbw.trackingappbackend.control.TileService;
import de.dhbw.trackingappbackend.entity.AppUser;
import de.dhbw.trackingappbackend.entity.location.Location;
import de.dhbw.trackingappbackend.entity.LocationRepository;
import de.dhbw.trackingappbackend.entity.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

@SpringBootTest
public class LocationRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    LocationRepository locationRepository;
    @Autowired
    TileService tileService;
    @Autowired
    CoordinateService coordinateService;

    @Test
    public void testFindAllLocationsByUser() {

        Optional<AppUser> appUserOptional = userRepository.findByEmail("test@test.de");
        assert appUserOptional.isPresent();

        String appUserId = appUserOptional.get().getId();
        List<Location> allLocations = locationRepository.findByAppUserId(appUserId);

        assert(allLocations.size() == 4);
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