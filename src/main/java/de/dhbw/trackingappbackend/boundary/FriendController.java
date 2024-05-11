package de.dhbw.trackingappbackend.boundary;

import de.dhbw.trackingappbackend.model.response.FriendDTO;
import de.dhbw.trackingappbackend.model.response.InviteDTO;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

//TODO Example Data

@Tag(name = "Friend Controller")
public interface FriendController {

    @SecurityRequirement(name="oauth2")
    @Operation(summary = "returns a List of UUIDs")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Authentication successful",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = FriendDTO.class)))),
            @ApiResponse(responseCode = "401", description = "Invalid credentials provided",
                    content = @Content(schema = @Schema(implementation = String.class),
                            examples = @ExampleObject(value = "Invalid credentials provided"))
            )
    })
    ResponseEntity<?> getFriends();

    @SecurityRequirement(name="oauth2")
    @Operation(summary = "return a List of pending invites")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "returns all invites with status 0 [0=pending, 1=accepted, -1=declined]",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = InviteDTO.class)))),
            @ApiResponse(responseCode = "200", description = "there are no invites with status 0 [0=pending, 1=accepted, -1=declined]",
                    content = @Content(schema = @Schema(implementation = String.class),
                            examples = @ExampleObject(value = "No pending invites"))
            )
    })
    ResponseEntity<?> getInvites();

    @SecurityRequirement(name="oauth2")
    @Operation(summary = "call this endpoint to accept or deny an invite")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "returns an updated List with all current friends",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = FriendDTO.class)))),
            @ApiResponse(responseCode = "400", description = "there are no invites with given uuid and status 0 [0=pending, 1=accepted, -1=declined]",
                    content = @Content(schema = @Schema(implementation = String.class),
                            examples = @ExampleObject(value = "There is no matching invite"))
            ),
            @ApiResponse(responseCode = "400", description = "wrong parameters")
    })
    ResponseEntity<?> updateInviteStatusForUser(@RequestParam("accept") boolean accept, @RequestParam("uuid") String uuid);

    @SecurityRequirement(name="oauth2")
    @Operation(summary = "removes the friendID, returns a List of the remaining UUIDs")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "returns an updated List with all remaining friends",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = FriendDTO.class)))),
            @ApiResponse(responseCode = "400")
    })
    ResponseEntity<?> deleteFriend(@PathVariable String friendID);

    @SecurityRequirement(name="oauth2")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "everything worked, request send"),
            @ApiResponse(responseCode = "400", description = "an error occurred")
    })
    @Operation(summary = "sends a request to the selectedUser")
    ResponseEntity<?> addFriendWithEmail(@PathVariable String email);
}
