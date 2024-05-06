package de.dhbw.trackingappbackend.boundary;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Location Controller")
public interface LocationController {

    @SecurityRequirement(name="oauth2")
    @Operation(summary = "Returns a list of locations of a user by given zoomLevel, starting from the given lat/lon coordinates as the center anchor point.")
    ResponseEntity<?> getLocations(@RequestParam double latitude, @RequestParam double longitude, @RequestParam byte zoomLevel);

    @SecurityRequirement(name="oauth2")
    @Operation(summary = "Adds the tile of the given lat/lon coordinates to the visited locations of a user.")
    ResponseEntity<?> addLocation(@RequestParam double latitude, @RequestParam double longitude);

    @SecurityRequirement(name="oauth2")
    @Operation(summary = "Returns a list of all locations of a user.")
    ResponseEntity<?> getAllLocations();
}
