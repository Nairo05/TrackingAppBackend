package de.dhbw.trackingappbackend.boundary;

import de.dhbw.trackingappbackend.control.LocationService;
import de.dhbw.trackingappbackend.entity.AppUser;
import de.dhbw.trackingappbackend.entity.UserRepository;
import de.dhbw.trackingappbackend.entity.location.LocationWrapper;
import de.dhbw.trackingappbackend.security.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
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

    private final LocationService locationService;

    @SecurityRequirement(name="oauth2")
    @Operation(summary = "Returns a list of locations of a user by given zoomLevel, starting from the given lat/lon coordinates as the west/south anchor point")
    @GetMapping("/locations")
    public ResponseEntity<?> getLocations(@RequestParam double latitude, @RequestParam double longitude, @RequestParam byte zoomLevel) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        Optional<AppUser> appUserOptional = userRepository.findById(userDetails.getId());

        if (zoomLevel != 14) return ResponseEntity.badRequest().body("Zoom level must be 14"); // TODO remove zoom restriction

        if (appUserOptional.isPresent()) {

            String appUserId = appUserOptional.get().getId();

            List<LocationWrapper> locations = locationService.getLocations(appUserId, latitude, longitude, zoomLevel);

            if (locations == null || locations.isEmpty()) {
                return ResponseEntity.ok("No locations visited. Go outside!");
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
