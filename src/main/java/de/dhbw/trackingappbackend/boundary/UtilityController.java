package de.dhbw.trackingappbackend.boundary;

import de.dhbw.trackingappbackend.entity.AppUser;
import de.dhbw.trackingappbackend.entity.UserRepository;
import de.dhbw.trackingappbackend.model.response.ShownUserModel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Tag(name = "Utility Controller", description = "collection of small endpoints")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/utility")
@RequiredArgsConstructor
public class UtilityController {

    private final UserRepository userRepository;

    @Operation(summary = "returns a List of User Objects with UUID and Name")
    @SecurityRequirement(name = "oauth2")
    @PostMapping("/identity")
    public ResponseEntity<List<ShownUserModel>> getNames(@RequestBody List<String> uuids) {

        List<AppUser> appUsers = userRepository.findAllById(uuids);

        List<ShownUserModel> shownUserModels = new ArrayList<>();

        appUsers.forEach(appUser -> {

            String shownName;

            if (appUser.getShownName() != null && !appUser.getShownName().isEmpty()) {
                shownName = appUser.getShownName();
            } else if (appUser.getFirstname() != null && !appUser.getFirstname().isEmpty()){
                shownName = appUser.getFirstname();
            } else {
                shownName = appUser.getEmail();
            }

            shownUserModels.add(new ShownUserModel(appUser.getId(), shownName));
        });

        return ResponseEntity.ok(shownUserModels);

    }
}
