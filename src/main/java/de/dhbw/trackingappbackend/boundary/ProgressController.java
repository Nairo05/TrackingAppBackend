package de.dhbw.trackingappbackend.boundary;

import de.dhbw.trackingappbackend.model.response.AchievementDTO;
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

import java.util.HashMap;

@Tag(name = "Progress Controller")
public interface ProgressController {

    @SecurityRequirement(name="oauth2")
    @Operation(summary = "Returns the percentage stats of the user's visited locations for germany and each Bundesland.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully returned stats.",
            content = @Content(schema = @Schema(implementation = HashMap.class, subTypes = {String.class, Float.class}),
                examples = @ExampleObject(value = "{\n" +
                    "  \"DE\": 0.0,\n" +
                    "  \"BW\": 0.0,\n" +
                    "  \"BY\": 0.0,\n" +
                    "  \"BE\": 0.0,\n" +
                    "  ...\n" +
                    "}"))
        ),
        @ApiResponse(responseCode = "401", description = "Invalid credentials provided.",
            content = @Content(schema = @Schema(implementation = String.class),
                examples = @ExampleObject(value = "Invalid credentials provided"))
        )
    })
    ResponseEntity<?> getStats();

    @SecurityRequirement(name="oauth2")
    @Operation(summary = "Returns all available achievements and if the user achieved them.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully returned achievements.",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = AchievementDTO.class)),
                examples = @ExampleObject(value = "[\n" +
                    "  {\n" +
                    "    \"title\": \"Sweet Sixteen\",\n" +
                    "    \"description\": \"Besuche jedes Bundesland in Deutschland.\",\n" +
                    "    \"achieved\": false\n" +
                    "  },\n" +
                    "  ...\n" +
                    "]")
        )),
        @ApiResponse(responseCode = "401", description = "Invalid credentials provided.",
            content = @Content(schema = @Schema(implementation = String.class),
                examples = @ExampleObject(value = "Invalid credentials provided"))
        )
    })
    ResponseEntity<?> getAchievements();
}