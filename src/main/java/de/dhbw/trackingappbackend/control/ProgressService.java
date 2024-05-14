package de.dhbw.trackingappbackend.control;

import de.dhbw.trackingappbackend.entity.AchievementRepository;
import de.dhbw.trackingappbackend.entity.LocationRepository;
import de.dhbw.trackingappbackend.entity.StatRepository;
import de.dhbw.trackingappbackend.entity.user.Achievement;
import de.dhbw.trackingappbackend.entity.user.Stat;
import de.dhbw.trackingappbackend.entity.user.UserRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Tag(name = "Location Service")
@Service
@RequiredArgsConstructor
public class ProgressService {

    private final UserRepository userRepository;
    private final LocationRepository locationRepository;
    private final StatRepository statRepository;
    private final AchievementRepository achievementRepository;

    public void updateStats(String appUserId, List<String> locationIds) {

        int countLocations = locationRepository.countLocationsByIdInAndKuerzelContains(locationIds, "DE");
    }

    public void updateAchievements(String appUserId) {

        // TODO add implementation
    }

    public Map<String, Float> createNewUserStats() {

        Map<String, Float> stats = new HashMap<>();

        // DE BW BY BE BB HB HH HE MV NI NW RP SL SN ST SH TH
        stats.put("DE", 0f);
        stats.put("BE", 0f);
        stats.put("BB", 0f);
        stats.put("BW", 0f);
        stats.put("BY", 0f);
        stats.put("HB", 0f);
        stats.put("HE", 0f);
        stats.put("HH", 0f);
        stats.put("MV", 0f);
        stats.put("NI", 0f);
        stats.put("NW", 0f);
        stats.put("RP", 0f);
        stats.put("SH", 0f);
        stats.put("SL", 0f);
        stats.put("SN", 0f);
        stats.put("ST", 0f);
        stats.put("TH", 0f);

        return stats;
    }

    public List<Achievement> createNewUserAchievements() {

        List<Achievement> achievements = new ArrayList<>();

        // DE BW BY BE BB HB HH HE MV NI NW RP SL SN ST SH TH
        achievements.add(new Achievement("ACHVMNT_", "Sweet Sixteen", "Besuche jedes Bundesland in Deutschland.", false));
        achievements.add(new Achievement("ACHVMNT_", "O'zapft is!", "Besuche Bayern.", false));
        achievements.add(new Achievement("ACHVMNT_", "Nett hier.", "Besuche Baden-Württemberg.", false));
        achievements.add(new Achievement("ACHVMNT_", "Dit is Berlin.", "Besuche Berlin.", false));
        achievements.add(new Achievement("ACHVMNT_", "Kann man nicht meckern.", "Besuche Brandenburg.", false));
        achievements.add(new Achievement("ACHVMNT_", "Wat mutt, dat mutt.", "Besuche Bremen.", false));
        achievements.add(new Achievement("ACHVMNT_", "Moin!", "Besuche Hamburg.", false));
        achievements.add(new Achievement("ACHVMNT_", "Gugg emol rum!", "Besuche Hessen.", false));
        achievements.add(new Achievement("ACHVMNT_", "'n mooien Dag wünsch wi di!", "Besuche Mecklenburg-Vorpommern.", false));
        achievements.add(new Achievement("ACHVMNT_", "Klar.", "Besuche Niedersachen.", false));
        achievements.add(new Achievement("ACHVMNT_", "Laot juh guet gaohn", "Besuche Nordrhein-Westfalen.", false));
        achievements.add(new Achievement("ACHVMNT_", "Allo hopp!", "Besuche Rheinland-Pfalz.", false));
        achievements.add(new Achievement("ACHVMNT_", "PLATZHALTER_TEXT", "Besuche das Saarland.", false));
        achievements.add(new Achievement("ACHVMNT_", "PLATZHALTER_TEXT", "Besuche Sachsen.", false));
        achievements.add(new Achievement("ACHVMNT_", "PLATZHALTER_TEXT", "Besuche Sachsen-Anhalt.", false));
        achievements.add(new Achievement("ACHVMNT_", "PLATZHALTER_TEXT", "Besuche Schleswig-Holstein.", false));
        achievements.add(new Achievement("ACHVMNT_", "PLATZHALTER_TEXT", "Besuche Thüringen.", false));

        achievementRepository.saveAll(achievements);

        return achievements;
    }
}
