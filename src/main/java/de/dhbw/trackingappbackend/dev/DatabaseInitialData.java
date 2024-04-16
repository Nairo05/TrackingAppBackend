package de.dhbw.trackingappbackend.dev;

import de.dhbw.trackingappbackend.entity.*;
import de.dhbw.trackingappbackend.entity.location.Location;
import de.dhbw.trackingappbackend.entity.location.LocationId;
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
                    List.of("A", "B", "C", "D"));

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

            locationRepository.saveAll(Arrays.asList(
                new Location(new LocationId(0, 0), appUser1.getId(), new double[]{0.0, 0.0}),
                new Location(new LocationId(0, 1), appUser1.getId(), new double[]{0.0, 0.5}),
                new Location(new LocationId(1, 0), appUser1.getId(), new double[]{0.5, 0.0}),
                new Location(new LocationId(1, 1), appUser1.getId(), new double[]{0.5, 0.5})
            ));
        };
    }
}