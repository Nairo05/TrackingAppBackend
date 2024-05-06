package de.dhbw.trackingappbackend.boundary;

import de.dhbw.trackingappbackend.control.CoordinateService;
import de.dhbw.trackingappbackend.control.TileService;
import de.dhbw.trackingappbackend.entity.AppUser;
import de.dhbw.trackingappbackend.entity.LocationRepository;
import de.dhbw.trackingappbackend.entity.UserRepository;
import de.dhbw.trackingappbackend.entity.location.Location;
import de.dhbw.trackingappbackend.entity.location.LocationWrapper;
import de.dhbw.trackingappbackend.entity.location.Tile;
import de.dhbw.trackingappbackend.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.geo.GeoJsonPolygon;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class LocationControllerImpl implements LocationController {

    private final UserRepository userRepository;

    private final LocationRepository locationRepository;
    private final CoordinateService coordinateService;

    @GetMapping("/locations")
    public ResponseEntity<?> getLocations(@RequestParam double latitude, @RequestParam double longitude, @RequestParam byte zoomLevel) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        Optional<AppUser> appUserOptional = userRepository.findById(userDetails.getId());

        if (zoomLevel != 14) return ResponseEntity.badRequest().body("Zoom level must be 14"); // TODO remove zoom restriction

        if (appUserOptional.isPresent()) {

            AppUser appUser = appUserOptional.get();
            List<String> locationIds = appUser.getLocationIds();

            // create request area polygon from given coordinates
            GeoJsonPolygon polygon = coordinateService.getGeoJsonPolygon(latitude, longitude, zoomLevel);

            // get locations within the polygon and the user's location ids
            List<Location> locations = locationRepository.findByTilePositionWithinAndIdIn(polygon, locationIds);

            if (locations == null || locations.isEmpty()) {
                return ResponseEntity.ok(Collections.emptyList());
            }
            else {
                return ResponseEntity.ok(locations.stream()
                    .map(LocationWrapper::new)
                    .toList());
            }
        }
        else {
            return ResponseEntity.badRequest().body("User not found!");
        }
    }

    @PostMapping("/location")
    public ResponseEntity<?> addLocation(@RequestParam double latitude, @RequestParam double longitude) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        Optional<AppUser> appUserOptional = userRepository.findById(userDetails.getId());

        if (appUserOptional.isPresent()) {

            AppUser appUser = appUserOptional.get();
            String appUserId = appUser.getId();

            Tile newTile = TileService.getTileByCoordinates(latitude, longitude, (byte) 14);

            Optional<Location> locationOptional = locationRepository.findByTile(newTile);

            if (locationOptional.isEmpty()) { // tile not in db > location outside germany
                return ResponseEntity.badRequest().body("Coordinates are not within Germany!");
            }

            Location newLocation = locationOptional.get();

            if (userRepository.existsByIdAndLocationIdsContains(appUserId, newLocation.getId())) {
                return ResponseEntity.badRequest().body("Location already visited!");
            }
            else {
                // add new location id to user
                // TODO easier way? directly in mongo?
                List<String> locationIds = appUser.getLocationIds();
                locationIds.add(newLocation.getId());
                appUser.setLocationIds(locationIds);
                userRepository.save(appUser);

                return ResponseEntity.ok(new LocationWrapper(newLocation)); // return newly visited location
            }
        }
        else {
            return ResponseEntity.badRequest().body("User not found!");
        }
    }

    @GetMapping("/locations/all")
    public ResponseEntity<?> getAllLocations() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        Optional<AppUser> appUserOptional = userRepository.findById(userDetails.getId());

        if (appUserOptional.isPresent()) {

            AppUser appUser = appUserOptional.get();
            List<String> locationIds = appUser.getLocationIds();

            // get locations within the polygon and the user's location ids
            List<Location> locations = locationRepository.findByIdIn(locationIds);

            if (locations == null || locations.isEmpty()) {
                return ResponseEntity.ok(Collections.emptyList());
            }
            else {
                return ResponseEntity.ok(locations.stream()
                    .map(LocationWrapper::new)
                    .toList());
            }
        }
        else {
            return ResponseEntity.badRequest().body("User not found!");
        }
    }
}
