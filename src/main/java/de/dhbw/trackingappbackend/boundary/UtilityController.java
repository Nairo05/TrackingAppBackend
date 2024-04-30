package de.dhbw.trackingappbackend.boundary;

import de.dhbw.trackingappbackend.model.response.ShownUserModel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Tag(name = "Utility Controller", description = "collection of small endpoints")
public interface UtilityController {

    @Operation(summary = "converts a List of UUIDs in a List of User Objects with UUID and Name")
    @SecurityRequirement(name = "oauth2")
    ResponseEntity<List<ShownUserModel>> getNames(@RequestBody List<String> uuids);

    @Operation(summary = "returns the friendID matching to the username")
    @SecurityRequirement(name = "oauth2")
    public ResponseEntity<String> getFriendID(@PathVariable String username);
}
