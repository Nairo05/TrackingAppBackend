package de.dhbw.trackingappbackend.boundary;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FingerprintController {



    ResponseEntity<?> savePrint() {
        return ResponseEntity.ok().build();
    }

}
