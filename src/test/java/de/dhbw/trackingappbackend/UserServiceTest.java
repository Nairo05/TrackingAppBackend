package de.dhbw.trackingappbackend;

import de.dhbw.trackingappbackend.entity.location.LocationRepository;
import de.dhbw.trackingappbackend.entity.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    UserRepository userRepository;
    @Autowired
    LocationRepository locationRepository;

    /*
    @Test
    public void testHasUserVisitedLocation() {

        Optional<AppUser> appUserOptional = userRepository.findByEmail("test@test.de");
        assert appUserOptional.isPresent();

        AppUser appUser = appUserOptional.get();
        String appUserId = appUser.getId();
        String locationId = appUser.getLocationIds().get(0);
        assert userRepository.existsByIdAndLocationIdsContains(appUserId, locationId);
    }*/
}
