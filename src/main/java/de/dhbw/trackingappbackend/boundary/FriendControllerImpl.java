package de.dhbw.trackingappbackend.boundary;

import de.dhbw.trackingappbackend.boundary.dto.FriendDTO;
import de.dhbw.trackingappbackend.boundary.dto.InviteDTO;
import de.dhbw.trackingappbackend.control.FriendService;
import de.dhbw.trackingappbackend.entity.user.AppUser;
import de.dhbw.trackingappbackend.entity.user.Friend;
import de.dhbw.trackingappbackend.entity.user.UserRepository;
import de.dhbw.trackingappbackend.security.UserDetailsImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import jdk.jfr.Frequency;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Tag(name = "Friend Controller")
@CrossOrigin(origins = "*", maxAge = 3600)
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class FriendControllerImpl implements FriendController {

    private final UserRepository userRepository;
    private final FriendService friendService;

    @Override
    @GetMapping("/friends")
    public ResponseEntity<?> getFriends() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        Optional<AppUser> appUserOptional = userRepository.findById(userDetails.getId());

        if (appUserOptional.isPresent()) {

            AppUser appUser = appUserOptional.get();

            if (appUser.getFriends() == null || appUser.getFriends().isEmpty()) {

                return ResponseEntity.ok("You have no friends");

            } else {

                List<Friend> friends = appUser.getFriends();

                friends = friends.stream().filter(f -> f.getStatus() == Friend.accepted).toList();

                return ResponseEntity.ok(friends.stream()
                        .map(friend ->
                                FriendDTO
                                        .builder()
                                        .uuid(friend.getUuid())
                                        .email(friend.getEmail())
                                        .acceptedAt(friend.getAcceptedAt().toString())
                                        .build())
                );
            }

        } else {

            return ResponseEntity.badRequest().build();

        }
    }

    @Override
    @GetMapping("/invites")
    public ResponseEntity<?> getInvites() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        Optional<AppUser> appUserOptional = userRepository.findById(userDetails.getId());

        if (appUserOptional.isPresent()) {

            AppUser appUser = appUserOptional.get();

            List<Friend> friends = appUser.getFriends();

            friends = friends.stream().filter(f -> f.getStatus() == Friend.open).toList();

            if (friends.isEmpty()) {

                return ResponseEntity.ok().body("No pending invites");

            } else {

                return ResponseEntity.ok(friends.stream()
                        .map(friend ->
                            InviteDTO
                                    .builder()
                                    .uuid(friend.getUuid())
                                    .email(friend.getEmail())
                                    .sendAt(friend.getSendAt().toString())
                                    .build()));
            }
        }

        return null;
    }

    @Override
    @PostMapping("/friend/update")
    public ResponseEntity<?> updateInviteStatusForUser(boolean accept, String uuid) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        Optional<AppUser> appUserOptional = userRepository.findById(userDetails.getId());

        if (appUserOptional.isPresent()) {

            AppUser appUser = appUserOptional.get();

            Friend friend = appUser.getFriends()
                    .stream().filter(friendFilter -> friendFilter.getStatus() == Friend.open && friendFilter.getUuid().equals(uuid))
                    .findFirst().orElse(null);

            if (friend == null || friend.getStatus() != Friend.open) {

                return ResponseEntity.badRequest().body("There is no matching invite");

            } else {

                appUser.getFriends().removeIf(itFriend -> itFriend.getUuid().equals(friend.getUuid()));

                if (accept) {
                    friend.setStatus(Friend.accepted);
                    appUser.getFriends().add(friend);
                }

                userRepository.save(appUser);

                return ResponseEntity.ok(appUser.getFriends().stream()
                        .map(friendIt ->
                                FriendDTO
                                        .builder()
                                        .uuid(friendIt.getUuid())
                                        .email(friendIt.getEmail())
                                        .acceptedAt(friendIt.getAcceptedAt().toString())
                                        .build())
                );
            }
        }
        return null;
    }

    @Override
    @DeleteMapping("/friend/{friendID}")
    public ResponseEntity<?> deleteFriend(@PathVariable String friendID) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        Optional<AppUser> appUserOptional = userRepository.findById(userDetails.getId());

        if (appUserOptional.isPresent()) {

            AppUser appUser = appUserOptional.get();

            if (appUser.getFriends() == null || appUser.getFriends().isEmpty()) {

                return ResponseEntity.ok("You have no friends");

            } else {

                appUser.setFriends(appUser.getFriends().stream().filter(friend -> !friend.getUuid().equals(friendID)).toList());
                userRepository.save(appUser);

                return ResponseEntity.ok(appUser.getFriends().stream()
                        .filter(f -> f.getStatus() == Friend.accepted)
                        .map(friend ->
                                FriendDTO
                                        .builder()
                                        .uuid(friend.getUuid())
                                        .email(friend.getEmail())
                                        .acceptedAt(friend.getAcceptedAt().toString())
                                        .build())
                );
            }

        } else {

            return ResponseEntity.badRequest().build();

        }
    }


    @Override
    @PostMapping("/friend/email/{email}")
    public ResponseEntity<?> addFriendWithEmail(@PathVariable String email) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        Optional<AppUser> appUserOptional = userRepository.findById(userDetails.getId());

        if (appUserOptional.isPresent()) {

            AppUser appUser = appUserOptional.get();

            Optional<AppUser> addUserOptional = userRepository.findByEmail(email);

            if (addUserOptional.isPresent()) {

                AppUser addUser = addUserOptional.get();

                Friend friend = new Friend(addUser.getId(), Friend.open, addUser.getEmail(), Instant.now(), null);

                appUser.getFriends().add(friend);

                userRepository.save(appUser);

                return ResponseEntity.ok().body("Request from " + addUser.getEmail() + " send to user " + appUser.getEmail());

            } else {

                return ResponseEntity.badRequest().body("Cant find any user with " + email);

            }

        }

        return ResponseEntity.badRequest().body("There was a Problem resolving the jwt, cant extract user from context");
    }

}


