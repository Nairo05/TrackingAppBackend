package de.dhbw.trackingappbackend;

import de.dhbw.trackingappbackend.entity.AppUser;
import de.dhbw.trackingappbackend.entity.LocationRepository;
import de.dhbw.trackingappbackend.entity.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    UserRepository userRepository;
    @Autowired
    LocationRepository locationRepository;

    @Test
    public void testHasUserVisitedLocation() {

        Optional<AppUser> appUserOptional = userRepository.findByEmail("horb@test.de");
        assert appUserOptional.isPresent();

        AppUser appUser = appUserOptional.get();
        String appUserId = appUser.getId();
        String locationId = appUser.getLocationIds().get(0);
        assert userRepository.existsByIdAndLocationIdsContains(appUserId, locationId);
    }
}
