package de.dhbw.trackingappbackend.entity;

import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "files")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FileEntity {

    @Id
    private String id;

    @Size(min = 1, max = 50)
    private String fileName;

    @Size(min = 1, max = 10)
    private String fileType;

    private byte[] data;

    private LocalDateTime created;

    private String ownerId;
}
