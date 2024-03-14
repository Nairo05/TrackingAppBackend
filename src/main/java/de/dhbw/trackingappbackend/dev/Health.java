package de.dhbw.trackingappbackend.dev;

import de.dhbw.trackingappbackend.entity.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public String database() {
        return userRepository.findById(2).toString();
    }

}
