package de.dhbw.trackingappbackend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
public class AppUser {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min=3, max = 20)
    private String firstname;

    @NotBlank
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

    @Override
    public String toString() {
        return "ID: " + this.getId() + " Firstname:" + this.getFirstname() + " Lastname:" + this.getLastname();
    }
}
