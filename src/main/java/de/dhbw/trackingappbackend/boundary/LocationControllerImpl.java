package de.dhbw.trackingappbackend.boundary;

import de.dhbw.trackingappbackend.control.CoordinateService;
import de.dhbw.trackingappbackend.control.LocationService;
import de.dhbw.trackingappbackend.control.TileService;
import de.dhbw.trackingappbackend.entity.user.AppUser;
import de.dhbw.trackingappbackend.entity.LocationRepository;
import de.dhbw.trackingappbackend.entity.user.UserRepository;
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
    private final LocationService locationService;
    private final CoordinateService coordinateService;

    @GetMapping("/locations")
    public ResponseEntity<?> getLocations(@RequestParam double latitude, @RequestParam double longitude, @RequestParam byte zoomLevel) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        Optional<AppUser> appUserOptional = userRepository.findById(userDetails.getId());

        if (14 < zoomLevel) return ResponseEntity.badRequest().body("Invalid zoomLevel provided"); // TODO add lower limit, dont return bad request but adjust manually

        if (appUserOptional.isPresent()) {

            AppUser appUser = appUserOptional.get();
            List<String> locationIds = appUser.getLocationIds();

            // create request area polygon from given coordinates
            GeoJsonPolygon polygon = coordinateService.getGeoJsonPolygon(latitude, longitude, zoomLevel);

            // get single locations within the polygon and the user's location ids
            List<Location> locations = locationRepository.findByTilePositionWithinAndIdIn(polygon, locationIds);

            if (locations.isEmpty()) { // no locations visited
                return ResponseEntity.ok(Collections.emptyList());
            }
            else {
                // adjust to zoom level
                List<LocationWrapper> zoomedLocations = locationService.zoomLocations(locations, zoomLevel);

                return ResponseEntity.ok(zoomedLocations);
            }
        }
        else {
            return ResponseEntity.badRequest().body("Invalid credentials provided");
        }
    }

    @GetMapping("/locations/merged")
    public ResponseEntity<?> getMergedLocations(@RequestParam double latitude, @RequestParam double longitude, @RequestParam byte zoomLevel) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        Optional<AppUser> appUserOptional = userRepository.findById(userDetails.getId());

        if (14 < zoomLevel) return ResponseEntity.badRequest().body("Invalid zoomLevel provided"); // TODO add lower limit, dont return bad request but adjust manually

        if (appUserOptional.isPresent()) {

            AppUser appUser = appUserOptional.get();
            List<String> locationIds = appUser.getLocationIds();

            // create request area polygon from given coordinates
            GeoJsonPolygon polygon = coordinateService.getGeoJsonPolygon(latitude, longitude, zoomLevel);

            // get single locations within the polygon and the user's location ids
            List<Location> locations = locationRepository.findByTilePositionWithinAndIdIn(polygon, locationIds);

            if (locations.isEmpty()) { // no locations visited
                return ResponseEntity.ok(Collections.emptyList());
            }
            else {
                // adjust to zoom level
                List<LocationWrapper> zoomedLocations = locationService.zoomLocations(locations, zoomLevel);

                // merge locations to bigger polygons
                List<LocationWrapper> mergedLocations = locationService.mergeLocations(zoomedLocations);

                return ResponseEntity.ok(mergedLocations);
            }
        }
        else {
            return ResponseEntity.badRequest().body("Invalid credentials provided");
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
                return ResponseEntity.ok().build(); // TODO not yet implemented
            }

            Location newLocation = locationOptional.get();

            if (userRepository.existsByIdAndLocationIdsContains(appUserId, newLocation.getId())) {
                return ResponseEntity.noContent().build(); // location already visited
            }
            else {
                // add new location id to user
                List<String> locationIds = appUser.getLocationIds();
                locationIds.add(newLocation.getId());
                appUser.setLocationIds(locationIds);
                userRepository.save(appUser);

                return ResponseEntity.ok().build();
            }
        }
        else {
            return ResponseEntity.badRequest().body("Invalid credentials provided");
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
            return ResponseEntity.badRequest().body("Invalid credentials provided");
        }
    }
}
