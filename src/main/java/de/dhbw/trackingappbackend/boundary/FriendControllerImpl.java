package de.dhbw.trackingappbackend.boundary;

import de.dhbw.trackingappbackend.entity.AppUser;
import de.dhbw.trackingappbackend.entity.UserRepository;
import de.dhbw.trackingappbackend.security.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Tag(name = "Friend Controller")
@CrossOrigin(origins = "*", maxAge = 3600)
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class FriendControllerImpl implements FriendController {

    private final UserRepository userRepository;

    @Override
    @GetMapping("/friends")
    public ResponseEntity<?> getFriends() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        Optional<AppUser> appUserOptional = userRepository.findById(userDetails.getId());

        if (appUserOptional.isPresent()) {

            AppUser appUser = appUserOptional.get();

            if (appUser.getFriendIDs() == null || appUser.getFriendIDs().isEmpty()) {

                return ResponseEntity.ok("You have no friends");

            } else {
                return ResponseEntity.ok(appUser.getFriendIDs());
            }

        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @Override
    @DeleteMapping("/friend/{friendID}")
    public ResponseEntity<?> deleteFriend(@PathVariable String friendID) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        Optional<AppUser> appUserOptional = userRepository.findById(userDetails.getId());

        if (appUserOptional.isPresent()) {

            AppUser appUser = appUserOptional.get();

            if (appUser.getFriendIDs() == null || appUser.getFriendIDs().isEmpty()) {

                return ResponseEntity.ok("You have no friends");

            } else {

                appUser.setFriendIDs(
                        appUser.getFriendIDs().stream()
                                .filter(friend -> !Objects.equals(friend, friendID))
                                .collect(Collectors.toList()));

                userRepository.save(appUser);

                return ResponseEntity.ok(appUser.getFriendIDs());

            }

        } else {
            return ResponseEntity.badRequest().build();
        }
    }

}


