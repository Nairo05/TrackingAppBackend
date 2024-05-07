package de.dhbw.trackingappbackend.entity.user;

import de.dhbw.trackingappbackend.entity.user.Friend;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashMap;
import java.util.List;

@Document(collection = "base")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
public class AppUser {

    @Id
    private String id;

    @Size(min=3, max = 20)
    private String firstname;

    @Size(min=3, max=20)
    private String lastname;

    @NotBlank
    @Size(max = 50)
    @Email
    @NonNull
    private String email;

    @NotBlank
    @NonNull
    @Size(min = 4, max = 120)
    private String username;

    @NotBlank
    @Size(min=8, max = 120)
    @NonNull
    private String password;

    List<Friend> friends;

    List<String> locationIds;

    String profilePictureId;

    @Override
    public String toString() {
        return "ID: " + this.getId() + " Firstname:" + this.getFirstname() + " Lastname:" + this.getLastname();
    }
}
