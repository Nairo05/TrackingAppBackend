package de.dhbw.trackingappbackend.control;

import de.dhbw.trackingappbackend.entity.AchievementRepository;
import de.dhbw.trackingappbackend.entity.StatRepository;
import de.dhbw.trackingappbackend.entity.location.Achievement;
import de.dhbw.trackingappbackend.entity.location.Stat;
import de.dhbw.trackingappbackend.entity.user.UserRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Tag(name = "Location Service")
@Service
@RequiredArgsConstructor
public class ProgressService {

    private final UserRepository userRepository;
    private final StatRepository statRepository;
    private final AchievementRepository achievementRepository;

    public void updateStats() {

        // TODO add implementation
    }

    public void updateAchievements() {

        // TODO add implementation
    }

    public List<String> createNewStats() {

        List<Stat> stats = new ArrayList<>();

        // DE BW BY BE BB HB HH HE MV NI NW RP SL SN ST SH TH
        stats.add(new Stat(UUID.randomUUID().toString(), "DE", "Deutschland", 0f));
        stats.add(new Stat(UUID.randomUUID().toString(), "BE", "Berlin", 0f));
        stats.add(new Stat(UUID.randomUUID().toString(), "BB", "Brandenburg", 0f));
        stats.add(new Stat(UUID.randomUUID().toString(), "BW", "Baden-Württemberg", 0f));
        stats.add(new Stat(UUID.randomUUID().toString(), "BY", "Bayern", 0f));
        stats.add(new Stat(UUID.randomUUID().toString(), "HB", "Bremen", 0f));
        stats.add(new Stat(UUID.randomUUID().toString(), "HE", "Hessen", 0f));
        stats.add(new Stat(UUID.randomUUID().toString(), "HH", "Hamburg", 0f));
        stats.add(new Stat(UUID.randomUUID().toString(), "MV", "Mecklenburg-Vorpommern", 0f));
        stats.add(new Stat(UUID.randomUUID().toString(), "NI", "Niedersachsen", 0f));
        stats.add(new Stat(UUID.randomUUID().toString(), "NW", "Nordrhein-Westfalen", 0f));
        stats.add(new Stat(UUID.randomUUID().toString(), "RP", "Reinland-Pfalz", 0f));
        stats.add(new Stat(UUID.randomUUID().toString(), "SH", "Schleswig-Holstein", 0f));
        stats.add(new Stat(UUID.randomUUID().toString(), "SL", "Saarland", 0f));
        stats.add(new Stat(UUID.randomUUID().toString(), "SN", "Sachsen", 0f));
        stats.add(new Stat(UUID.randomUUID().toString(), "ST", "Sachsen-Anhalt", 0f));
        stats.add(new Stat(UUID.randomUUID().toString(), "TH", "Thüringen", 0f));

        statRepository.saveAll(stats);

        return stats.stream().map(Stat::getId).toList();
    }

    public List<String> createNewAchievements() {

        List<Achievement> achievements = new ArrayList<>();

        achievements.add(new Achievement(UUID.randomUUID().toString(),
            "Sweet Sixteen", "Besuche jedes Bundesland in Deutschland.", false));
        achievements.add(new Achievement(UUID.randomUUID().toString(),
            "O'zapft is!", "Besuche Bayern.", false));
        achievements.add(new Achievement(UUID.randomUUID().toString(),
            "Nett hier.", "Besuche Baden-Württemberg.", false));
        achievements.add(new Achievement(UUID.randomUUID().toString(),
            "Dit is Berlin.", "Besuche Berlin.", false));
        achievements.add(new Achievement(UUID.randomUUID().toString(),
            "Kann man nicht meckern.", "Besuche Brandenburg.", false));
        achievements.add(new Achievement(UUID.randomUUID().toString(),
            "Wat mutt, dat mutt.", "Besuche Bremen.", false));
        achievements.add(new Achievement(UUID.randomUUID().toString(),
            "Moin!", "Besuche Hamburg.", false));
        achievements.add(new Achievement(UUID.randomUUID().toString(),
            "Gugg emol rum!", "Besuche Hessen.", false));
        achievements.add(new Achievement(UUID.randomUUID().toString(),
            "'n mooien Dag wünsch wi di!", "Besuche Mecklenburg-Vorpommern.", false));
        achievements.add(new Achievement(UUID.randomUUID().toString(),
            "Klar.", "Besuche Niedersachen.", false));
        achievements.add(new Achievement(UUID.randomUUID().toString(),
            "Laot juh guet gaohn", "Besuche Nordrhein-Westfalen.", false));
        achievements.add(new Achievement(UUID.randomUUID().toString(),
            "Allo hopp!", "Besuche Rheinland-Pfalz.", false));
        achievements.add(new Achievement(UUID.randomUUID().toString(),
            "PLATZHALTER_TEXT", "Besuche das Saarland.", false));
        achievements.add(new Achievement(UUID.randomUUID().toString(),
            "PLATZHALTER_TEXT", "Besuche Sachsen.", false));
        achievements.add(new Achievement(UUID.randomUUID().toString(),
            "PLATZHALTER_TEXT", "Besuche Sachsen-Anhalt.", false));
        achievements.add(new Achievement(UUID.randomUUID().toString(),
            "PLATZHALTER_TEXT", "Besuche Schleswig-Holstein.", false));
        achievements.add(new Achievement(UUID.randomUUID().toString(),
            "PLATZHALTER_TEXT", "Besuche Thüringen.", false));

        achievementRepository.saveAll(achievements);

        return achievements.stream().map(Achievement::getId).toList();
    }
}
