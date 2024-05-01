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

        List<String> locationIds = appUserOptional.get().getLocationIds();
        List<Location> allLocations = locationRepository.findByIdIn(locationIds);

        assert(allLocations.size() == 4);
    }
}
