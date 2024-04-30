package de.dhbw.trackingappbackend.boundary;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "Friend Controller")
public interface FriendController {

    @SecurityRequirement(name="oauth2")
    @Operation(summary = "returns a List of UUIDs")
    ResponseEntity<?> getFriends();

    @SecurityRequirement(name="oauth2")
    @Operation(summary = "removes the firendID, returns a List of the remaining UUIDs")
    ResponseEntity<?> deleteFriend(@PathVariable String friendID);

    @SecurityRequirement(name="oauth2")
    @Operation(summary = "adds the firendID, returns a List of the remaining UUIDs")
    ResponseEntity<?> addFriend(@PathVariable String friendID);
}
