package de.dhbw.trackingappbackend.dev;

import de.dhbw.trackingappbackend.entity.AppUser;
import de.dhbw.trackingappbackend.entity.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("dev")
public class DatabaseInitialData {

    @Bean
    public CommandLineRunner createTestData(UserRepository userRepository){
        return (args) -> {
            userRepository.save(new AppUser(1L,"firstname1","lastname1", "test1@test.de","Passwort123."));
            userRepository.save(new AppUser(2L,"firstname2","lastname2", "test2@test.de","Passwort123."));
            userRepository.save(new AppUser(3L,"firstname3","lastname3", "test3@test.de","Passwort123."));
        };
    }

}
