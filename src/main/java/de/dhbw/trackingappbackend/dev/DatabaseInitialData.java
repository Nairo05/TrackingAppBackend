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
            String uuid1 = UUID.randomUUID().toString();
            String uuid2 = UUID.randomUUID().toString();
            String uuid3 = UUID.randomUUID().toString();
            String uuid4 = UUID.randomUUID().toString();


            AppUser appUser = AppUser.builder()
                    .email("test1@test.de")
                    .id(uuid1)
                    .password(password)
                    .username("test1Username")
                    .firstname("TestFirstName")
                    .lastname("TestLastName")
                    .friends(List.of(
                            new Friend(uuid2, Friend.accepted, "test2@test.de", Instant.now(), Instant.now()),
                            new Friend(uuid3, Friend.accepted, "test3@test.de", Instant.now(), Instant.now()),
                            new Friend(uuid4, Friend.open, "test4@test.de", Instant.now(), Instant.now())
                            ))
                    .locationIds(Collections.emptyList())
                    .profilePictureId(null)
                    .build();

            AppUser appUser1 = AppUser.builder()
                    .email("test2@test.de")
                    .id(uuid2)
                    .password(password)
                    .username("test2Username")
                    .firstname("Test2FirstName")
                    .lastname("Test2LastName")
                    .friends(Collections.emptyList())
                    .locationIds(Collections.emptyList())
                    .profilePictureId(null)
                    .build();

            AppUser appUser2 = AppUser.builder()
                    .email("test3@test.de")
                    .id(uuid3)
                    .password(password)
                    .username("test3Username")
                    .firstname("Test3FirstName")
                    .lastname("Test3LastName")
                    .friends(Collections.emptyList())
                    .locationIds(Collections.emptyList())
                    .profilePictureId(null)
                    .build();

            AppUser appUserNoFriends = AppUser.builder()
                    .email("test4@test.de")
                    .id(uuid4)
                    .password(password)
                    .username("test4Username")
                    .firstname("Test4FirstName")
                    .lastname("Test4LastName")
                    .friends(Collections.emptyList())
                    .locationIds(Collections.emptyList())
                    .profilePictureId(null)
                    .build();


            userRepository.save(appUser);
            userRepository.save(appUser1);
            userRepository.save(appUser2);
            userRepository.save(appUserNoFriends);
        };
    }
}