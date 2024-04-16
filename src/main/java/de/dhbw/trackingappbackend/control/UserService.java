package de.dhbw.trackingappbackend.control;

import de.dhbw.trackingappbackend.entity.UserRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Tag(name = "User Service")
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public boolean hasUserVisitedLocation(String appUserId, String locationId) {
        return userRepository.existsByIdAndLocationIdsContains(appUserId, locationId);
    }
}
