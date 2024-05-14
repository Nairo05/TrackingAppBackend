package de.dhbw.trackingappbackend.control;

import de.dhbw.trackingappbackend.entity.AchievementRepository;
import de.dhbw.trackingappbackend.entity.LocationRepository;
import de.dhbw.trackingappbackend.entity.user.UserRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(name = "Location Service")
@Service
@RequiredArgsConstructor
public class ProgressService {

    private final UserRepository userRepository;
    private final LocationRepository locationRepository;
    private final AchievementRepository achievementRepository;

    public void updateStats(Map<String, Float> stats, List<String> locationIds) {

        int countUserDE = locationRepository.countLocationsByIdInAndKuerzelContains(locationIds, "DE");
        int countAllDE = locationRepository.countLocationsByKuerzelContains("DE"); // TODO pre-safe total counts?
        stats.put("DE", (float) countUserDE / countAllDE);

        // TODO add bundeslaender percentages
    }

    public void updateAchievements(List<String> achievements, List<String> locationIds) {

        // TODO add achievement implementation
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
}
