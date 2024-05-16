package de.dhbw.trackingappbackend.control;

import de.dhbw.trackingappbackend.entity.Achievement;
import de.dhbw.trackingappbackend.entity.AchievementRepository;
import de.dhbw.trackingappbackend.entity.location.Location;
import de.dhbw.trackingappbackend.entity.location.LocationRepository;
import de.dhbw.trackingappbackend.entity.location.Tile;
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

    public void updateProgress(Map<String, Float> stats, List<String> achievements, Location location, List<String> locationIds) {

        updateStatsByKuerzelList(stats, locationIds, location.getKuerzel());
        updateAchievements(achievements, location.getKuerzel(), location.getTile());
    }

    public void updateStatsByKuerzel(Map<String, Float> stats, List<String> locationIds, String kuerzel) {

        int countTotal = TOTAL_TILE_COUNTS.get(kuerzel);
        int countUser = locationRepository.countAllByIdInAndKuerzelContains(locationIds, kuerzel);
        stats.put(kuerzel, (float) countUser / countTotal);
    }

    public void updateStatsByKuerzelList(Map<String, Float> stats, List<String> locationIds, List<String> kuerzelList) {

        for (String kuerzel : kuerzelList) {
            updateStatsByKuerzel(stats, locationIds, kuerzel);
        }
    }

    public void updateAchievements(List<String> achievements, List<String> newLocationKuerzel, Tile newLocationTile) {

        List<Achievement> allAchievements = achievementRepository.findAll();

        for (Achievement achvmnt : allAchievements) {

            if (achvmnt.getKuerzel() != null) {
                if (newLocationKuerzel.contains(achvmnt.getKuerzel()) && !achievements.contains(achvmnt.getId())) {
                    achievements.add(achvmnt.getId());
                }
            }
            else if (achvmnt.getTile() != null) {
                if (achvmnt.getTile().getXTile() == newLocationTile.getXTile() &&
                    achvmnt.getTile().getYTile() == newLocationTile.getYTile() &&
                    !achievements.contains(achvmnt.getId())) {
                    achievements.add(achvmnt.getId());
                }
            }
        }
    }

    public void updateAllStats(Map<String, Float> stats, List<String> locationIds) {

        for (String kuerzel : ALL_KUERZEL) {
            updateStatsByKuerzel(stats, locationIds, kuerzel);
        }
    }

    public void updateAllAchievements(List<String> achievementIds, List<String> locationIds) {

        List<Location> locations = locationRepository.findByIdIn(locationIds);
        List<Achievement> achievements = achievementRepository.findAll();

        for (Achievement achvmnt : achievements) {
            for (Location location : locations) {
                if (achvmnt.getKuerzel() != null) {
                    if (location.getKuerzel().contains(achvmnt.getKuerzel()) && !achievementIds.contains(achvmnt.getId())) {
                        achievementIds.add(achvmnt.getId());
                        break;
                    }
                }
                else if (achvmnt.getTile() != null) {
                    if (location.getTile().getXTile() == achvmnt.getTile().getXTile() &&
                        location.getTile().getYTile() == achvmnt.getTile().getYTile() &&
                        !achievementIds.contains(achvmnt.getId())) {
                            achievementIds.add(achvmnt.getId());
                            break;
                    }
                }
            }
        }
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
