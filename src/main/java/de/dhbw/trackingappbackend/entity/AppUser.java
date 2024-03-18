package de.dhbw.trackingappbackend.entity;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.validator.constraints.UniqueElements;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "base")
@Data
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
    @Size(min=8, max = 120)
    @NonNull
    private String password;

    private String shownName;

    List<String> friendIDs;

    List<String> locationIDs;

    @Override
    public String toString() {
        return "ID: " + this.getId() + " Firstname:" + this.getFirstname() + " Lastname:" + this.getLastname();
    }
}
