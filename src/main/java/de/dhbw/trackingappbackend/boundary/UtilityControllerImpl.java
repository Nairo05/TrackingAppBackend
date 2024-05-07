package de.dhbw.trackingappbackend.boundary;

import de.dhbw.trackingappbackend.entity.user.AppUser;
import de.dhbw.trackingappbackend.entity.user.UserRepository;
import de.dhbw.trackingappbackend.model.response.ShownUserModel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/utility")
@RequiredArgsConstructor
public class UtilityControllerImpl implements UtilityController{

    private final UserRepository userRepository;

    @Override
    @PostMapping("/identity")
    public ResponseEntity<List<ShownUserModel>> getNames(@RequestBody List<String> uuids) {

        List<AppUser> appUsers = userRepository.findAllById(uuids);

        List<ShownUserModel> shownUserModels = new ArrayList<>();

        appUsers.forEach(appUser -> {

            String shownName;

            if (!appUser.getUsername().isEmpty()) {
                shownName = appUser.getUsername();
            } else if (!appUser.getEmail().isEmpty()){
                shownName = appUser.getFirstname();
            } else {
                shownName = appUser.getId();
            }

            shownUserModels.add(new ShownUserModel(appUser.getId(), shownName));
        });

        return ResponseEntity.ok(shownUserModels);

    }

    @Override
    @GetMapping("/fid")
    public ResponseEntity<String> getFriendID(String username) {

        Optional<AppUser> appUser = userRepository.findByUsername(username);

        return appUser.map(user -> ResponseEntity.ok(user.getId())).orElseGet(() -> ResponseEntity
                .badRequest()
                .body("cant find username"));
    }
}
