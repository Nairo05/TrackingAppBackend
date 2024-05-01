package de.dhbw.trackingappbackend.boundary;

import de.dhbw.trackingappbackend.control.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
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
}