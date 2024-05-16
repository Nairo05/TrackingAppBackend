package de.dhbw.trackingappbackend.dev;

import de.dhbw.trackingappbackend.entity.AchievementRepository;
import de.dhbw.trackingappbackend.entity.FileEntity;
import de.dhbw.trackingappbackend.entity.FileRepository;
import de.dhbw.trackingappbackend.entity.location.LocationRepository;
import de.dhbw.trackingappbackend.entity.Achievement;
import de.dhbw.trackingappbackend.entity.user.AppUser;
import de.dhbw.trackingappbackend.entity.user.Friend;
import de.dhbw.trackingappbackend.entity.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.*;

@Configuration
@Profile("dev")
@RequiredArgsConstructor
public class DatabaseInitialData {

    private final PasswordEncoder encoder;

    @Bean
    public CommandLineRunner createTestData(UserRepository userRepository, LocationRepository locationRepository, FileRepository fileRepository, AchievementRepository achievementRepository) {
        return (args) -> {

            String filePath = "src/main/resources/images/neuronalesNetz.png";
            byte[] byteArray;

            try {
                byteArray = Files.readAllBytes(Paths.get(filePath));
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }

            userRepository.deleteAll();
            fileRepository.deleteAll();

            String password = encoder.encode("password123.");

            String uuid1 = UUID.randomUUID().toString();
            String uuid2 = UUID.randomUUID().toString();
            String uuid3 = UUID.randomUUID().toString();
            String uuid4 = UUID.randomUUID().toString();
            String uuidPicture1 = UUID.randomUUID().toString();


            FileEntity fileEntity = new FileEntity(
                    uuidPicture1,
                    "Unbenannt.png",
                    "image/png",
                    byteArray,
                    Instant.now(),
                    uuid1
            );

            fileRepository.save(fileEntity);

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
                    .stats(new HashMap<>())
                    .achievementIds(Collections.emptyList())
                    .locationIds(Collections.emptyList())
                    .profilePictureId(uuidPicture1)
                    .build();

            AppUser appUser1 = AppUser.builder()
                    .email("test2@test.de")
                    .id(uuid2)
                    .password(password)
                    .username("test2Username")
                    .firstname("Test2FirstName")
                    .lastname("Test2LastName")
                    .friends(Collections.emptyList())
                    .stats(new HashMap<>())
                    .achievementIds(Collections.emptyList())
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
                    .stats(new HashMap<>())
                    .achievementIds(Collections.emptyList())
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
                    .stats(new HashMap<>())
                    .achievementIds(Collections.emptyList())
                    .locationIds(Collections.emptyList())
                    .profilePictureId(null)
                    .build();

            userRepository.save(appUser);
            userRepository.save(appUser1);
            userRepository.save(appUser2);
            userRepository.save(appUserNoFriends);

            // DE BW BY BE BB HB HH HE MV NI NW RP SL SN ST SH TH
            achievementRepository.save(new Achievement("ACHVMNT_DE", "Sweet Sixteen", "Besuche jedes Bundesland in Deutschland."));
            achievementRepository.save(new Achievement("ACHVMNT_BY", "O'zapft is!", "Besuche Bayern."));
            achievementRepository.save(new Achievement("ACHVMNT_BW", "Nett hier.", "Besuche Baden-Württemberg."));
            achievementRepository.save(new Achievement("ACHVMNT_BE", "Dit is Berlin.", "Besuche Berlin."));
            achievementRepository.save(new Achievement("ACHVMNT_BB", "Kann man nicht meckern.", "Besuche Brandenburg."));
            achievementRepository.save(new Achievement("ACHVMNT_HB", "Wat mutt, dat mutt.", "Besuche Bremen."));
            achievementRepository.save(new Achievement("ACHVMNT_HH", "Moin!", "Besuche Hamburg."));
            achievementRepository.save(new Achievement("ACHVMNT_HE", "Gugg emol rum!", "Besuche Hessen."));
            achievementRepository.save(new Achievement("ACHVMNT_MV", "'n mooien Dag wünsch wi di!", "Besuche Mecklenburg-Vorpommern."));
            achievementRepository.save(new Achievement("ACHVMNT_NI", "Klar.", "Besuche Niedersachen."));
            achievementRepository.save(new Achievement("ACHVMNT_NW", "Laot juh guet gaohn", "Besuche Nordrhein-Westfalen."));
            achievementRepository.save(new Achievement("ACHVMNT_RP", "Allo hopp!", "Besuche Rheinland-Pfalz."));
            achievementRepository.save(new Achievement("ACHVMNT_SL", "PLATZHALTER_TEXT", "Besuche das Saarland."));
            achievementRepository.save(new Achievement("ACHVMNT_SN", "PLATZHALTER_TEXT", "Besuche Sachsen."));
            achievementRepository.save(new Achievement("ACHVMNT_ST", "PLATZHALTER_TEXT", "Besuche Sachsen-Anhalt."));
            achievementRepository.save(new Achievement("ACHVMNT_SH", "PLATZHALTER_TEXT", "Besuche Schleswig-Holstein."));
            achievementRepository.save(new Achievement("ACHVMNT_TH", "PLATZHALTER_TEXT", "Besuche Thüringen."));
        };
    }
}