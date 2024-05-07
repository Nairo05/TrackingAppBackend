package de.dhbw.trackingappbackend.control;

import de.dhbw.trackingappbackend.entity.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FriendService {

    private final UserRepository userRepository;

    public boolean isValidFriendID(String friendID) {

        return userRepository.existsById(friendID);
    }

}
