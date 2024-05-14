package de.dhbw.trackingappbackend.boundary;

import de.dhbw.trackingappbackend.entity.location.LocationWrapper;
import de.dhbw.trackingappbackend.entity.Achievement;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Location Controller")
public interface LocationController {

    @SecurityRequirement(name="oauth2")
    @Operation(summary = "Returns a list of location tiles of a user by given zoomLevel, starting from the given lat/lon coordinates as the center anchor point.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully returned locations.",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = LocationWrapper.class)))
            ),
            @ApiResponse(responseCode = "401", description = "Invalid credentials provided.",
                    content = @Content(schema = @Schema(implementation = String.class),
                            examples = @ExampleObject(value = "Invalid credentials provided"))
            )
    })
    ResponseEntity<?> getLocations(@RequestParam double latitude, @RequestParam double longitude, @RequestParam byte zoomLevel);

    @SecurityRequirement(name="oauth2")
    @Operation(summary = "Returns a list of merged location polygons of a user by given zoomLevel, starting from the given lat/lon coordinates as the center anchor point.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully returned merged locations.",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = LocationWrapper.class)))
            ),
            @ApiResponse(responseCode = "401", description = "Invalid credentials provided.",
                    content = @Content(schema = @Schema(implementation = String.class),
                            examples = @ExampleObject(value = "Invalid credentials provided"))
            )
    })
    ResponseEntity<?> getMergedLocations(@RequestParam double latitude, @RequestParam double longitude, @RequestParam byte zoomLevel);

    @SecurityRequirement(name="oauth2")
    @Operation(summary = "Adds the tile of the given lat/lon coordinates to the visited locations of a user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully added new location.",
                    content = @Content(schema = @Schema(implementation = LocationWrapper.class))
            ),
            @ApiResponse(responseCode = "401", description = "Invalid credentials provided.",
                    content = @Content(schema = @Schema(implementation = String.class),
                            examples = @ExampleObject(value = "Invalid credentials provided"))
            )
    })
    ResponseEntity<?> addLocation(@RequestParam double latitude, @RequestParam double longitude);
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully returned all locations.",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = LocationWrapper.class)))
            ),
            @ApiResponse(responseCode = "401", description = "Invalid credentials provided.",
                    content = @Content(schema = @Schema(implementation = String.class),
                            examples = @ExampleObject(value = "Invalid credentials provided"))
            )
    })
    @SecurityRequirement(name="oauth2")
    @Operation(summary = "Returns a list of all locations of a user.")
    ResponseEntity<?> getAllLocations();
}
