package de.dhbw.trackingappbackend.boundary;

import de.dhbw.trackingappbackend.entity.AppUser;
import de.dhbw.trackingappbackend.entity.Location;
import de.dhbw.trackingappbackend.entity.LocationRepository;
import de.dhbw.trackingappbackend.entity.UserRepository;
import de.dhbw.trackingappbackend.security.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.data.geo.Box;
import org.springframework.data.geo.Point;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Tag(name = "Location Controller")
@CrossOrigin(origins = "*", maxAge = 3600)
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class LocationController {

    private final UserRepository userRepository;

    private final LocationRepository locationRepository;

    @SecurityRequirement(name="oauth2")
    @Operation(summary = "returns a List of Locations within given Box")
    @GetMapping("/locations")
    public ResponseEntity<?> getLocations(@RequestParam double firstLon, @RequestParam double firstLat, @RequestParam double secondLon, @RequestParam double secondLat) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        Optional<AppUser> appUserOptional = userRepository.findById(userDetails.getId());

        if (appUserOptional.isPresent()) {

            String appUserId = appUserOptional.get().getId();
            Box b = new Box(new Point(firstLon, firstLat), new Point(secondLon, secondLat));

            List<Location> locations = locationRepository.findByAppUserIdAndPositionWithin(appUserId, b);

            if (locations == null || locations.isEmpty()) {
                return ResponseEntity.ok("Go outside");
            }
            else {
                return ResponseEntity.ok(locations);
            }
        }
        else {
            return ResponseEntity.badRequest().body("User not found");
        }
    }
}
