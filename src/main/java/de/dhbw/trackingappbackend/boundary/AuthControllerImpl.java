package de.dhbw.trackingappbackend.boundary;

import de.dhbw.trackingappbackend.control.AuthService;
import de.dhbw.trackingappbackend.model.request.LoginRequest;
import de.dhbw.trackingappbackend.model.request.RegisterRequest;
import de.dhbw.trackingappbackend.model.response.ForgotPasswordModel;
import de.dhbw.trackingappbackend.model.response.ResetPasswordModel;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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

        if (registerRequest.getEmail() != null) {

            if (authService.isEmailTaken(registerRequest.getEmail())) {
                return ResponseEntity
                        .badRequest()
                        .body("Email is already taken");
            }

        } else {

            return ResponseEntity
                    .badRequest()
                    .body("Email must be filled");

        }

        if (registerRequest.getUsername() != null) {

            if (authService.isUserNameTaken(registerRequest.getUsername())) {
                return ResponseEntity
                        .badRequest()
                        .body("UserName is already taken");
            }
        }

        authService.registerUser(registerRequest);

        return ResponseEntity.ok(("User registered successfully!"));
    }

    @Override
    @PostMapping("/login")
    public ResponseEntity<?> login(
            @Valid @RequestBody LoginRequest loginRequest,
            @RequestHeader("X-Login-Type") String type) {

        if (type == null || !type.equals("UserPasswordAuth")) {
            return ResponseEntity
                    .badRequest()
                    .body("Cant parse Login-Method by Filter. Wrong endpoint. Missing the corresponding Header-Type");
        }

        if (loginRequest.getEmail() != null) {

            authService.emailLogin(loginRequest.getEmail(), loginRequest.getPassword());

        } else if (loginRequest.getUsername() != null) {

            authService.userNameLogin(loginRequest.getUsername(), loginRequest.getPassword());

        }

        return ResponseEntity.ok(authService.generateJWTResponseFromContext());
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
    @PostMapping("/fingerprint/{email}")
    public ResponseEntity<?> authMe(@PathVariable String email, @RequestBody String fingerprint) {

        authService.fingerPrintLogin(email, fingerprint);

        //TODO add new Authentification extends AbstractAuthentification

        return ResponseEntity.ok().build();
    }


    @Override
    @PostMapping("/reset")
    public ResponseEntity<?> forgotPassword(@Valid @RequestBody ForgotPasswordModel forgotPasswordModel) {

        if (authService.resetPasswordRequest(forgotPasswordModel.getEmail())) {
            return ResponseEntity.ok().body("email sent");
        }

        return ResponseEntity.badRequest().build();
    }

    @Override
    @PostMapping("/reset/accepted")
    public ResponseEntity<?> forgotPasswordAccepted(@Valid @RequestBody ResetPasswordModel resetPasswordModel) {
        return ResponseEntity.status(501).build();
    }
}
