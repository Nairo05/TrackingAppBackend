package de.dhbw.trackingappbackend.control;

import de.dhbw.trackingappbackend.entity.AchievementRepository;
import de.dhbw.trackingappbackend.entity.location.LocationRepository;
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

    private final LocationRepository locationRepository;
    private final AchievementRepository achievementRepository;

    private final Map<String, Integer> TOTAL_TILE_COUNTS = createTotalTileCounts();
    private final List<String> ALL_KUERZEL = List.of("DE", "BE", "BB", "BW", "BY", "HB", "HE", "HH", "MV", "NI", "NW", "RP", "SH", "SL", "SN", "ST", "TH");

    public void updateStatByKuerzel(Map<String, Float> stats, List<String> locationIds, String kuerzel) {

        int countTotal = TOTAL_TILE_COUNTS.get(kuerzel);
        int countUser = locationRepository.countAllByIdInAndKuerzelContains(locationIds, kuerzel);
        stats.put(kuerzel, (float) countUser / countTotal);
    }

    public void updateStatByKuerzelList(Map<String, Float> stats, List<String> locationIds, List<String> kuerzelList) {

        for (String kuerzel : kuerzelList) {
            updateStatByKuerzel(stats, locationIds, kuerzel);
        }
    }

    public void updateAllStats(Map<String, Float> stats, List<String> locationIds) {

        for (String kuerzel : ALL_KUERZEL) {
            updateStatByKuerzel(stats, locationIds, kuerzel);
        }
    }

    public void updateAchievements(List<String> achievements, List<String> locationIds) {

        // TODO add achievement implementation, comes with bundeslaender -.-
    }

    public Map<String, Float> createNewUserStats(List<String> locationIds) {

        Map<String, Float> stats = new HashMap<>();
        updateAllStats(stats, locationIds);
        return stats;
    }

    private Map<String, Integer> createTotalTileCounts() {

        Map<String, Integer> totals = new HashMap<>();

        totals.put("DE", 154537);
        totals.put("BE", 484);
        totals.put("BB", 1155);
        totals.put("BW", 14246);
        totals.put("BY", 28178);
        totals.put("HB", 260);
        totals.put("HE", 9221);
        totals.put("HH", 437);
        totals.put("MV", 11883);
        totals.put("NI", 22738);
        totals.put("NW", 15266);
        totals.put("RP", 8390);
        totals.put("SH", 8280);
        totals.put("SL", 1137);
        totals.put("SN", 8214);
        totals.put("ST", 9472);
        totals.put("TH", 7224);

        return totals;
    }
}
