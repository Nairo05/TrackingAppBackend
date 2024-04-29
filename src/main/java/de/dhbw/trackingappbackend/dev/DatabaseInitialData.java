package de.dhbw.trackingappbackend.dev;

import de.dhbw.trackingappbackend.entity.AppUser;
import de.dhbw.trackingappbackend.entity.LocationRepository;
import de.dhbw.trackingappbackend.entity.UserRepository;
import de.dhbw.trackingappbackend.entity.location.Location;
import de.dhbw.trackingappbackend.entity.location.Tile;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Configuration
@Profile("dev")
@RequiredArgsConstructor
public class DatabaseInitialData {

    private final PasswordEncoder encoder;

    @Bean
    public CommandLineRunner createTestData(UserRepository userRepository, LocationRepository locationRepository) {
        return (args) -> {

            userRepository.deleteAll();
            locationRepository.deleteAll();

            String password = encoder.encode("password123.");

            AppUser appUser1 = new AppUser(
                    UUID.randomUUID().toString(),
                    "firstname1",
                    "lastname1",
                    "test1@test.de",
                    password,
                    "ShownName1",
                    Collections.emptyList(),
                    Collections.emptyList());

            AppUser appUser2 = new AppUser(
                    UUID.randomUUID().toString(),
                    "firstname2",
                    "lastname2",
                    "tes21@test.de",
                    password,
                    "ShownName2",
                    Collections.emptyList(),
                    Collections.emptyList());

            userRepository.save(appUser1);
            userRepository.save(appUser2);

            userRepository.save(new AppUser(
                    UUID.randomUUID().toString(),
                    "firstname4",
                    "lastname4",
                    "test4@test.de",
                    password,
                    "ShownName3",
                    List.of(appUser1.getId(), appUser2.getId()),
                    Collections.emptyList()));

            userRepository.save(new AppUser(
                    UUID.randomUUID().toString(),
                    "firstname3",
                    "lastname3",
                    "test3@test.de",
                    password,
                    "ShownName3",
                    List.of(appUser2.getId()),
                    Collections.emptyList()));

            AppUser testUser = new AppUser(
                UUID.randomUUID().toString(),
                "Test",
                "User",
                "test@test.de",
                password,
                "Test Nutzer",
                Collections.emptyList(),
                Collections.emptyList());

            Location loc1 = new Location(UUID.randomUUID().toString(), new Tile(0, 0, (byte) 14));
            Location loc2 = new Location(UUID.randomUUID().toString(), new Tile(0, 1, (byte) 14));
            Location loc3 = new Location(UUID.randomUUID().toString(), new Tile(1, 0, (byte) 14));
            Location loc4 = new Location(UUID.randomUUID().toString(), new Tile(1, 1, (byte) 14));

            locationRepository.saveAll(Arrays.asList(loc1, loc2, loc3, loc4));

            testUser.setLocationIds(Arrays.asList(loc1.getId(), loc2.getId(), loc3.getId(), loc4.getId()));

            userRepository.save(testUser);
        };
    }
}