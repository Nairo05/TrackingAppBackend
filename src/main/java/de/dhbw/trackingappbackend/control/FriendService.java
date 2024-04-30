package de.dhbw.trackingappbackend.control;

import de.dhbw.trackingappbackend.entity.AppUser;
import de.dhbw.trackingappbackend.entity.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FriendService {

    private final UserRepository userRepository;

    public boolean isValidFriendID(String friendID) {

        return userRepository.existsById(friendID);
    }

}
