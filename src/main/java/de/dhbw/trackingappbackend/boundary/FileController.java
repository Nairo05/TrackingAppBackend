package de.dhbw.trackingappbackend.boundary;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "File Controller", description = "handles every File like Profile Pictures")
public interface FileController {

    @Operation(summary = "accepts and consumes a Multipart stream and stores the corresponding file, returns a location uri")
    @SecurityRequirement(name="oauth2")
    ResponseEntity<?> uploadFileForUser(
            @RequestParam("file") MultipartFile multipartFile,
            @RequestParam("type") String fileType,
            @RequestParam("name") String fileName,
            @RequestParam("size") int size,
            @RequestParam(defaultValue = "false") boolean isProfilePicture) throws Exception;


    @Operation(summary = "produces image/jpeg containing the profile picture")
    @SecurityRequirement(name="oauth2")
    ResponseEntity<?> getProfilePicture() throws Exception;
}
