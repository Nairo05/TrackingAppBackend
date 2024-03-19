package de.dhbw.trackingappbackend.boundary;

import de.dhbw.trackingappbackend.control.AuthService;
import de.dhbw.trackingappbackend.model.request.LoginRequest;
import de.dhbw.trackingappbackend.model.request.RegisterRequest;
import de.dhbw.trackingappbackend.model.response.ForgotPasswordModel;
import de.dhbw.trackingappbackend.model.response.JwtResponse;
import de.dhbw.trackingappbackend.model.response.ResetPasswordModel;
import de.dhbw.trackingappbackend.security.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthControllerImpl implements AuthController {

    private final AuthService authService;

    @Override
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        if (authService.isEmailTaken(registerRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body("Username is already taken");
        }

        authService.registerUser(registerRequest);

        return ResponseEntity.ok(("User registered successfully!"));
    }

    @Override
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {

        authService.login(loginRequest.getEmail(), loginRequest.getPassword());

        return ResponseEntity.ok(authService.generateJWTResponse());
    }

    @Override
    @PostMapping("/logout")
    public ResponseEntity<?> logout() {

        authService.logout();

        return ResponseEntity.ok("logged out, bye");
    }

    @Override
    @GetMapping("/status")
    public ResponseEntity<?> status() {

        try {
            return ResponseEntity.ok(authService.getUserFromContext());
        } catch (Exception e) {
            return ResponseEntity.ok("Not authenticated");
        }

    }

    @Override
    @PostMapping("/token/refresh")
    public ResponseEntity<?> refreshToken(@RequestHeader String authorization, @RequestBody String refresh) {

        System.out.println(authorization);

        UUID uuid;

        try {
            uuid = UUID.fromString(refresh);

            return ResponseEntity.ok(authService.refreshToken(uuid, authorization));

        } catch (Exception e) {

            return ResponseEntity.badRequest().body("Invalid Refresh-Token");
        }

    }

    @Override
    @PostMapping("/reset")
    public ResponseEntity<?> forgotPassword(@Valid @RequestBody ForgotPasswordModel forgotPasswordModel) {
        return ResponseEntity.status(501).build();
    }

    @Override
    @PostMapping("/reset/accepted")
    public ResponseEntity<?> forgotPasswordAccepted(@Valid @RequestBody ResetPasswordModel resetPasswordModel) {
        return ResponseEntity.status(501).build();
    }

}
