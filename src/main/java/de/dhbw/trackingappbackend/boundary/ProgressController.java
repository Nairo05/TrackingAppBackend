package de.dhbw.trackingappbackend.boundary;

import de.dhbw.trackingappbackend.model.response.AchievementDTO;
import de.dhbw.trackingappbackend.model.response.StatDTO;
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
    @Operation(summary = "Returns the kuerzel and percentage of the user's visited Bundeslaender and sum of germany.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully returned stats.",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = StatDTO.class)),
                examples = @ExampleObject(value = "[\n" +
                    "  {\n" +
                    "    \"kuerzel\": \"DE\",\n" +
                    "    \"percentage\": 49.15438156\n" +
                    "  },\n" +
                    "  ...\n" +
                    "]"))
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