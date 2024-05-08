package de.dhbw.trackingappbackend.boundary;

import de.dhbw.trackingappbackend.control.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserControllerImpl implements UserController {

    private final UserService userService;

    @Override
    @PostMapping("/password/set")
    public Boolean changePassword(@RequestBody String password) {
        return userService.setPassword(password);
    }

    @Override
    public ResponseEntity<?> addAuthFingerprint() {
        return null;
    }
}
