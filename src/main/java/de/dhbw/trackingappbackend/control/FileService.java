package de.dhbw.trackingappbackend.control;

import de.dhbw.trackingappbackend.entity.user.AppUser;
import de.dhbw.trackingappbackend.entity.FileEntity;
import de.dhbw.trackingappbackend.entity.FileRepository;
import de.dhbw.trackingappbackend.entity.user.UserRepository;
import de.dhbw.trackingappbackend.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileService {

    private final FileRepository fileRepository;
    private final UserRepository userRepository;


    public UriComponents uploadProfilePicture(String fileName, String fileType, MultipartFile multipartFile) throws Exception {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Optional<AppUser> appUser = userRepository.findById(userDetails.getId());

        if (appUser.isEmpty()) {
            throw new Exception();
        }

        if (fileRepository.findAllByOwnerId(userDetails.getId()).size() > 2) {
            throw new Exception();
        }

        FileEntity fileEntity = FileEntity
                .builder()
                .id(UUID.randomUUID().toString())
                .created(LocalDateTime.now())
                .ownerId(userDetails.getId())
                .fileName(fileName)
                .fileType(fileType)
                .data(multipartFile.getBytes())
                .build();
        fileRepository.save(fileEntity);

        AppUser user = appUser.get();

        if (user.getProfilePictureId() != null) {

            fileRepository.deleteById(user.getProfilePictureId());

        }

        user.setProfilePictureId(fileEntity.getId());

        userRepository.save(user);

        return UriComponentsBuilder.newInstance()
                .queryParam("file", fileEntity.getId())
                .queryParam("type",fileType)
                .queryParam("name", fileName)
                .queryParam("size", multipartFile.getSize())
                .queryParam("isProfilePicture", true)
                .build();

    }

    public Resource getProfilePicture() throws Exception {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        Optional<FileEntity> fileEntity = fileRepository.findFirstByOwnerId(userDetails.getId());

        if (fileEntity.isEmpty()) {
            throw new Exception();
        }

        return new ByteArrayResource(fileEntity.get().getData());
    }

    public Resource getProfilePictureByUUID(String uuid) throws Exception {

        Optional<FileEntity> fileEntity = fileRepository.findFirstByOwnerId(uuid);

        if (fileEntity.isEmpty()) {
            throw new Exception();
        }

        return new ByteArrayResource(fileEntity.get().getData());
    }
}
