package de.dhbw.trackingappbackend.dev;

import de.dhbw.trackingappbackend.entity.AppUser;
import de.dhbw.trackingappbackend.entity.UserRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Test Controller", description = "for testing purpose, public context, dev profile needs to be active")
@Profile("dev")
@RestController
@RequestMapping("/dev")
@RequiredArgsConstructor
public class Health {

    private final UserRepository userRepository;

    @GetMapping("/health")
    public boolean health() {
        return true;
    }

    @GetMapping("/database")
    public List<AppUser> database() {
        return userRepository.findAll();
    }

}
