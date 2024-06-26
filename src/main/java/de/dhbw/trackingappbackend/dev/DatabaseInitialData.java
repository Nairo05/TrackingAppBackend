package de.dhbw.trackingappbackend.dev;

import de.dhbw.trackingappbackend.entity.AchievementRepository;
import de.dhbw.trackingappbackend.entity.FileEntity;
import de.dhbw.trackingappbackend.entity.FileRepository;
import de.dhbw.trackingappbackend.entity.location.LocationRepository;
import de.dhbw.trackingappbackend.entity.Achievement;
import de.dhbw.trackingappbackend.entity.location.Tile;
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
            achievementRepository.deleteAll();

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
                            new Friend(uuid2, Friend.accepted, "test2@test.de", Instant.now(), Instant.now(), 0f, "test2Username"),
                            new Friend(uuid4, Friend.open, "test4@test.de", Instant.now(), Instant.now(), 0f, "test4Username")
                            ))
                    .stats(new HashMap<>(){{ put("DE", 0f); }})
                    .achievementIds(Collections.emptyList())
                    .locationIds(Collections.emptyList())
                    .profilePictureId(uuidPicture1)
                    .cipher("889bb645-9e39-4685-a351-99cdd65a1c9f")
                    .build();

            AppUser appUser1 = AppUser.builder()
                    .email("test2@test.de")
                    .id(uuid2)
                    .password(password)
                    .username("test2Username")
                    .firstname("Test2FirstName")
                    .lastname("Test2LastName")
                    .friends(List.of(new Friend(uuid1, Friend.accepted, "test1@test.de", Instant.now(), Instant.now(), 0f, "test1Username")))
                    .stats(new HashMap<>(){{ put("DE", 0f); }})
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
                    .stats(new HashMap<>(){{ put("DE", 0f); }})
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
                    .stats(new HashMap<>(){{ put("DE", 0f); }})
                    .achievementIds(Collections.emptyList())
                    .locationIds(Collections.emptyList())
                    .profilePictureId(null)
                    .build();

            userRepository.save(appUser);
            userRepository.save(appUser1);
            userRepository.save(appUser2);
            userRepository.save(appUserNoFriends);

            // DE BW BY BE BB HB HH HE MV NI NW RP SL SN ST SH TH
            achievementRepository.save(new Achievement("ACHVMNT_BY", "O'zapft is!", "Besuche Bayern.", "BY", null));
            achievementRepository.save(new Achievement("ACHVMNT_BW", "Nett hier.", "Besuche Baden-Württemberg.", "BW", null));
            achievementRepository.save(new Achievement("ACHVMNT_BE", "Dit is Berlin.", "Besuche Berlin.", "BE", null));
            achievementRepository.save(new Achievement("ACHVMNT_BB", "Kann man nicht meckern.", "Besuche Brandenburg.", "BB", null));
            achievementRepository.save(new Achievement("ACHVMNT_HB", "Wat mutt, dat mutt.", "Besuche Bremen.", "HB", null));
            achievementRepository.save(new Achievement("ACHVMNT_HH", "Moin!", "Besuche Hamburg.", "HH", null));
            achievementRepository.save(new Achievement("ACHVMNT_HE", "Ach, wie schee!", "Besuche Hessen.", "HE", null));
            achievementRepository.save(new Achievement("ACHVMNT_MV", "'n mooien Dag wünsch wi di!", "Besuche Mecklenburg-Vorpommern.", "MV", null));
            achievementRepository.save(new Achievement("ACHVMNT_NI", "Na klar!", "Besuche Niedersachen.", "NI", null));
            achievementRepository.save(new Achievement("ACHVMNT_NW", "Laot juh guet gaohn", "Besuche Nordrhein-Westfalen.", "NW", null));
            achievementRepository.save(new Achievement("ACHVMNT_RP", "Allo hopp!", "Besuche Rheinland-Pfalz.", "RP", null));
            achievementRepository.save(new Achievement("ACHVMNT_SL", "Hauptsach gudd gess!", "Besuche das Saarland.", "SL", null));
            achievementRepository.save(new Achievement("ACHVMNT_SN", "Daach!", "Besuche Sachsen.", "SN", null));
            achievementRepository.save(new Achievement("ACHVMNT_ST", "Morschn!", "Besuche Sachsen-Anhalt.", "ST", null));
            achievementRepository.save(new Achievement("ACHVMNT_SH", "Dat is dat Lunn", "Besuche Schleswig-Holstein.", "SH", null));
            achievementRepository.save(new Achievement("ACHVMNT_TH", "Na, wie 'en?", "Besuche Thüringen.", "TH", null));

            achievementRepository.save(new Achievement("ACHVMNT_DHBW", "Studentenleben", "Besuche die DHBW Campus Horb.", null, new Tile(8587, 5664, (byte) 14)));
        };
    }
}