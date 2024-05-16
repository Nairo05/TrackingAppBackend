package de.dhbw.trackingappbackend.boundary;

import de.dhbw.trackingappbackend.control.ProgressService;
import de.dhbw.trackingappbackend.entity.AchievementRepository;
import de.dhbw.trackingappbackend.entity.user.AppUser;
import de.dhbw.trackingappbackend.entity.user.UserRepository;
import de.dhbw.trackingappbackend.model.response.AchievementDTO;
import de.dhbw.trackingappbackend.model.response.StatDTO;
import de.dhbw.trackingappbackend.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class ProgressControllerImpl implements ProgressController {

    private final ProgressService progressService;
    private final UserRepository userRepository;
    private final AchievementRepository achievementRepository;

    @GetMapping("/stats")
    public ResponseEntity<?> getStats() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        Optional<AppUser> appUserOptional = userRepository.findById(userDetails.getId());

        if (appUserOptional.isPresent()) {

            AppUser appUser = appUserOptional.get();
            Map<String, Float> stats;

            if (appUser.getStats() == null || appUser.getStats().isEmpty()) { // create new stats if don't exist
                stats = progressService.createNewUserStats();
            } else {
                stats = appUser.getStats();
            }

            // update stats
            progressService.updateStats(stats, appUser.getLocationIds());
            appUser.setStats(stats);
            userRepository.save(appUser);

            return ResponseEntity.ok(stats.keySet().stream()
                .map(key -> StatDTO.builder()
                    .kuerzel(key)
                    .percentage(stats.get(key) * 100f)
                    .build())
                .toList());
        }
        else {
            return ResponseEntity.badRequest().body("Invalid credentials provided");
        }
    }

    @GetMapping("/achievements")
    public ResponseEntity<?> getAchievements() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        Optional<AppUser> appUserOptional = userRepository.findById(userDetails.getId());

        if (appUserOptional.isPresent()) {

            AppUser appUser = appUserOptional.get();
            List<String> achievementIds = appUser.getAchievementIds();

            // update achievements
            progressService.updateAchievements(achievementIds, appUser.getLocationIds());
            appUser.setAchievementIds(achievementIds);
            userRepository.save(appUser);

            // collect achieved and not achieved achievements
            List<AchievementDTO> achievementsDto = new ArrayList<>(achievementRepository.findAchievementsByIdIn(achievementIds)
                .stream()
                .map(AchievementDTO::new)
                .peek(AchievementDTO::setAchieved)
                .toList());
            achievementsDto.addAll(achievementRepository.findAchievementsByIdNotIn(achievementIds)
                .stream()
                .map(AchievementDTO::new)
                .toList());

            return ResponseEntity.ok(achievementsDto);        }
        else {
            return ResponseEntity.badRequest().body("Invalid credentials provided");
        }
    }
}