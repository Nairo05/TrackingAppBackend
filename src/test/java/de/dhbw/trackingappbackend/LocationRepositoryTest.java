package de.dhbw.trackingappbackend;

import de.dhbw.trackingappbackend.control.CoordinateService;
import de.dhbw.trackingappbackend.control.TileService;
import de.dhbw.trackingappbackend.entity.location.LocationRepository;
import de.dhbw.trackingappbackend.entity.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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

    /*
    @Test
    public void testFindAllLocationsByUser() {

        Optional<AppUser> appUserOptional = userRepository.findByEmail("test@test.de");
        assert appUserOptional.isPresent();

        List<String> locationIds = appUserOptional.get().getLocationIds();
        List<Location> allLocations = locationRepository.findByIdIn(locationIds);

        assert(allLocations.size() == 4);
    }*/
}
