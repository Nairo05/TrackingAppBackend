package de.dhbw.trackingappbackend.boundary;

import de.dhbw.trackingappbackend.model.request.LoginRequest;
import de.dhbw.trackingappbackend.model.request.RegisterRequest;
import de.dhbw.trackingappbackend.model.response.JwtResponse;
import de.dhbw.trackingappbackend.model.response.ResetPasswordModel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@Tag(name = "Authentication Controller", description = "handles authentication and user management")
public interface AuthController {


    @Operation(summary = "register a new User with an email. If username field ist not filled, username=email")
    ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest registerRequest);

    @Operation(summary = "login as an existing user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Authentication successful",
                    content = @Content(schema = @Schema(implementation = JwtResponse.class))),
            @ApiResponse(responseCode = "401", description = "Invalid credentials provided")
    })
    ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest, @RequestHeader("X-Login-Type") String type);


    @Operation(summary = "log out the current user and delete all (User-)Token")
    @SecurityRequirement(name="oauth2")
    ResponseEntity<?> logout();

    @Operation(summary = "get status details about the current user")
    @SecurityRequirement(name="oauth2")
    ResponseEntity<?> status();

    @Operation(summary = "sends an email to reset the password. Email contains a Pin")
    ResponseEntity<?> forgotPassword(@RequestBody String email);

    @Operation(summary = "needs the pin from the email, resets the password")
    ResponseEntity<?> forgotPasswordAccepted(@Valid @RequestBody ResetPasswordModel resetPasswordModel);

    @Operation(summary = "exchange the refresh token for a new JWT")
    ResponseEntity<?> refreshToken(@RequestHeader String token, String bearer);

    @Operation(summary = "use biometric fingerprint information to login")
    public ResponseEntity<?> registerFingerPrint(@RequestBody String token);
}
