package de.dhbw.trackingappbackend.dev;

import de.dhbw.trackingappbackend.entity.user.AppUser;
import de.dhbw.trackingappbackend.entity.FileRepository;
import de.dhbw.trackingappbackend.entity.LocationRepository;
import de.dhbw.trackingappbackend.entity.user.UserRepository;
import de.dhbw.trackingappbackend.entity.user.Friend;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
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
            fileRepository.deleteAll();

            String password = encoder.encode("password123.");

            AppUser appUser1 = new AppUser(
                    UUID.randomUUID().toString(),
                    "firstname1",
                    "lastname1",
                    "datev@test.de",
                    "username1",
                    password,
                    List.of(new Friend("1",Friend.open, "email@email.de",
                                    Instant.now(), Instant.now()),
                            new Friend("2",Friend.accepted, "email2@email.de",
                                    Instant.now(), Instant.now()),
                            new Friend("3",Friend.accepted, "email2@email.de11",
                                    Instant.now(), Instant.now()),
                             new Friend("4",Friend.accepted, "email2@email.de1212",
                                    Instant.now(), Instant.now()),
                            new Friend("5",Friend.accepted, "email2@email.de222",
                                    Instant.now(), Instant.now())),
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