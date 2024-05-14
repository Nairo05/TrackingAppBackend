package de.dhbw.trackingappbackend.boundary;

import de.dhbw.trackingappbackend.control.ProgressService;
import de.dhbw.trackingappbackend.entity.AchievementRepository;
import de.dhbw.trackingappbackend.entity.StatRepository;
import de.dhbw.trackingappbackend.entity.location.Achievement;
import de.dhbw.trackingappbackend.entity.location.Stat;
import de.dhbw.trackingappbackend.entity.user.AppUser;
import de.dhbw.trackingappbackend.entity.user.UserRepository;
import de.dhbw.trackingappbackend.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class ProgressControllerImpl implements ProgressController {

    private final ProgressService progressService;

    private final UserRepository userRepository;
    private final StatRepository statRepository;
    private final AchievementRepository achievementRepository;

    @GetMapping("/stats")
    public ResponseEntity<?> getStats() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        Optional<AppUser> appUserOptional = userRepository.findById(userDetails.getId());

        if (appUserOptional.isPresent()) {

            AppUser appUser = appUserOptional.get();
            List<String> statIds = appUser.getStatIds();

            if (statIds.isEmpty()) { // create new stats if don't exist

                statIds = progressService.createNewStats();
                appUser.setStatIds(statIds);
                userRepository.save(appUser);
                progressService.updateStats();
            }

            List<Stat> stats = statRepository.findStatsByIdIn(statIds);

            return ResponseEntity.ok(stats);
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

            if (achievementIds.isEmpty()) { // create new achievements if don't exist

                achievementIds = progressService.createNewAchievements();
                appUser.setAchievementIds(achievementIds);
                userRepository.save(appUser);
                progressService.updateAchievements();
            }

            List<Achievement> achievements = achievementRepository.findAchievementsByIdIn(achievementIds);

            return ResponseEntity.ok(achievements);        }
        else {
            return ResponseEntity.badRequest().body("Invalid credentials provided");
        }
    }
}
