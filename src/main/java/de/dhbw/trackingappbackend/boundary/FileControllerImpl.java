package de.dhbw.trackingappbackend.boundary;

import de.dhbw.trackingappbackend.control.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponents;

import java.util.Objects;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/files")
@RequiredArgsConstructor
public class FileControllerImpl implements FileController {

    private final FileService fileService;

    @Override
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, path = "/upload")
    public ResponseEntity<?> uploadFileForUser(
            @RequestParam("file") MultipartFile multipartFile,
            @RequestParam("type") String fileType,
            @RequestParam("name") String fileName,
            @RequestParam("size") int size,
            @RequestParam(defaultValue = "false") boolean isProfilePicture) throws Exception {

        if (isProfilePicture) {

            if (!Objects.equals(multipartFile.getContentType(), fileType)
                    || !Objects.equals(multipartFile.getOriginalFilename(), fileName)
                    || multipartFile.getSize() != size) {

                return ResponseEntity
                        .badRequest()
                        .body(String.format("incorrect meta data stream { %s %s %s } uri { %s %s %s }",
                                multipartFile.getOriginalFilename(),
                                multipartFile.getContentType(),
                                multipartFile.getSize(),
                                fileName,
                                fileType,
                                size));
            }

            UriComponents uri = fileService.uploadProfilePicture(fileName, fileType, multipartFile);

            return ResponseEntity
                    .created(uri.toUri())
                    .build();

        } else {

            return ResponseEntity
                    .badRequest()
                    .body("not supported");
        }
    }

    @Override
    @GetMapping("/profile/picture")
    public ResponseEntity<?> getProfilePicture() throws Exception {

        Resource res = fileService.getProfilePicture();

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(res);
    }

    @Override
    @GetMapping("/profile/{uuid}/picture")
    public ResponseEntity<?> getProfilePictureForSpecificUser(String uuid) throws Exception {

        Resource res = fileService.getProfilePictureByUUID(uuid);

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(res);
    }
}
