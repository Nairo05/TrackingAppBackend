package de.dhbw.trackingappbackend.dev;

import de.dhbw.trackingappbackend.entity.AppUser;
import de.dhbw.trackingappbackend.entity.FileRepository;
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
    public CommandLineRunner createTestData(UserRepository userRepository, LocationRepository locationRepository, FileRepository fileRepository) {
        return (args) -> {

            userRepository.deleteAll();
            locationRepository.deleteAll();
            fileRepository.deleteAll();

            String password = encoder.encode("35dsf2dcf3");

            AppUser appUser1 = new AppUser(
                    UUID.randomUUID().toString(),
                    "firstname1",
                    "lastname1",
                    "test1@test.de",
                    "username1",
                    password,
                    Collections.emptyList(),
                    Collections.emptyList(),
                    null);

            AppUser appUser2 = new AppUser(
                    UUID.randomUUID().toString(),
                    "firstname2",
                    "lastname2",
                    "test2@test.de",
                    "username2",
                    password,
                    Collections.emptyList(),
                    Collections.emptyList(),
                    null);

            userRepository.save(appUser1);
            userRepository.save(appUser2);
        };
    }
}