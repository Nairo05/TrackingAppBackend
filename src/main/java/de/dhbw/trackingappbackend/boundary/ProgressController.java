package de.dhbw.trackingappbackend.boundary;

import de.dhbw.trackingappbackend.entity.user.Achievement;
import de.dhbw.trackingappbackend.entity.user.Stat;
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

@Tag(name = "Progress Controller")
public interface ProgressController {

    @SecurityRequirement(name="oauth2")
    @Operation(summary = "Returns the percentage stats of the user's visited locations for germany and each Bundesland.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully returned stats.",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = Stat.class)))
        ),
        @ApiResponse(responseCode = "401", description = "Invalid credentials provided.",
            content = @Content(schema = @Schema(implementation = String.class),
                examples = @ExampleObject(value = "Invalid credentials provided"))
        )
    })
    ResponseEntity<?> getStats();

    @SecurityRequirement(name="oauth2")
    @Operation(summary = "Returns the achievements and if a boolean value if the user achieved them.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully returned achievements.",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = Achievement.class)))
        ),
        @ApiResponse(responseCode = "401", description = "Invalid credentials provided.",
            content = @Content(schema = @Schema(implementation = String.class),
                examples = @ExampleObject(value = "Invalid credentials provided"))
        )
    })
    ResponseEntity<?> getAchievements();
}
