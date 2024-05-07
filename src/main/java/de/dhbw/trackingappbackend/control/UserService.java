package de.dhbw.trackingappbackend.control;

import de.dhbw.trackingappbackend.entity.user.AppUser;
import de.dhbw.trackingappbackend.entity.user.UserRepository;
import de.dhbw.trackingappbackend.security.UserDetailsImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Tag(name = "User Service")
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    public boolean hasUserVisitedLocation(String appUserId, String locationId) {
        return userRepository.existsByIdAndLocationIdsContains(appUserId, locationId);
    }

    /** returns true if the password was set **/
    public boolean setPassword(String password) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        String username = userDetails.getUsername();

        Optional<AppUser> appUser = userRepository.findByUsername(username);

        if (appUser.isEmpty()) {
            return false;
        }

        AppUser finalAppUser = appUser.get();
        finalAppUser.setPassword(encoder.encode(password));

        userRepository.save(finalAppUser);

        return true;
    }
}
