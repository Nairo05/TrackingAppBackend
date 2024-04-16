package de.dhbw.trackingappbackend.boundary;

import de.dhbw.trackingappbackend.control.CoordinateService;
import de.dhbw.trackingappbackend.entity.AppUser;
import de.dhbw.trackingappbackend.entity.location.Location;
import de.dhbw.trackingappbackend.entity.LocationRepository;
import de.dhbw.trackingappbackend.entity.UserRepository;
import de.dhbw.trackingappbackend.security.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.geo.GeoJsonPolygon;
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

    private final CoordinateService coordinateService;

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
            GeoJsonPolygon polygon = coordinateService.getGeoJsonPolygon(latitude, longitude, zoomLevel);

            List<Location> locations = locationRepository.findByAppUserIdAndPositionWithin(appUserId, polygon);

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


    @SecurityRequirement(name="oauth2")
    @Operation(summary = "Returns a List of Locations within given Box of the two Lat/Lon Coordinate Pairs")
    @GetMapping("/locations/all")
    public ResponseEntity<?> getAllLocations(@RequestParam double lon1, @RequestParam double lat1, @RequestParam double lon2, @RequestParam double lat2) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        Optional<AppUser> appUserOptional = userRepository.findById(userDetails.getId());

        if (appUserOptional.isPresent()) {

            String appUserId = appUserOptional.get().getId();
            GeoJsonPolygon polygon = coordinateService.getGeoJsonPolygon(lon1, lat1, lon2, lat2);

            List<Location> locations = locationRepository.findByAppUserIdAndPositionWithin(appUserId, polygon);

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
