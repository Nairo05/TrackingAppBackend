package de.dhbw.trackingappbackend.boundary;

import de.dhbw.trackingappbackend.control.AuthService;
import de.dhbw.trackingappbackend.entity.AppUser;
import de.dhbw.trackingappbackend.entity.UserRepository;
import de.dhbw.trackingappbackend.model.request.LoginRequest;
import de.dhbw.trackingappbackend.model.request.RegisterRequest;
import de.dhbw.trackingappbackend.model.response.ForgotPasswordModel;
import de.dhbw.trackingappbackend.model.response.JwtResponse;
import de.dhbw.trackingappbackend.model.response.ResetPasswordModel;
import de.dhbw.trackingappbackend.security.JwtUtils;
import de.dhbw.trackingappbackend.security.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Authentication Controller")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final AuthService authService;

    @Operation(summary = "register a new User")
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest signUpRequest) {
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body("Username is already taken");
        }

        authService.registerUser(signUpRequest);

        return ResponseEntity.ok(("User registered successfully!"));
    }

    @Operation(summary = "logs out the current user and deletes all Token")
    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        String currentUser = getUserFromContext().getId();

        authService.logout(currentUser);

        return ResponseEntity.ok("logged out");
    }

    @Operation(summary = "login as an existing user")
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        return ResponseEntity.ok(authService.generateJWTResponse(userDetails.getId(), userDetails.getEmail()));
    }

    @Operation(summary = "get status details about the current user")
    @SecurityRequirement(name="oauth2")
    @GetMapping("/status")
    public ResponseEntity<?> status() {

        try {
            return ResponseEntity.ok(getUserFromContext());
        } catch (Exception e) {
            return ResponseEntity.ok("Not authenticated");
        }

    }

    @Operation(summary = "sends an email to reset the password")
    @PostMapping("/reset")
    public ResponseEntity<?> forgotPassword(@Valid @RequestBody ForgotPasswordModel forgotPasswordModel) {
        return ResponseEntity.status(501).build();
    }

    @Operation(summary = "needs the token from the email, resets the password")
    @PostMapping("/reset/accepted")
    public ResponseEntity<?> forgotPasswordAccepted(@Valid @RequestBody ResetPasswordModel resetPasswordModel) {
        return ResponseEntity.status(501).build();
    }

    private UserDetailsImpl getUserFromContext() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return (UserDetailsImpl) authentication.getPrincipal();

    }
}
