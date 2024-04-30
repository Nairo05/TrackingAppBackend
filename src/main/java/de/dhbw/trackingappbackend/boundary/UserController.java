package de.dhbw.trackingappbackend.boundary;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "User Controller", description = "handles Requests for authenticated Users")
public interface UserController {

    @Operation(summary = "change the password of the current User, JWT in header is required")
    @SecurityRequirement(name="oauth2")
    Boolean changePassword(@RequestBody String password);

}
