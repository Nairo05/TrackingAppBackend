package de.dhbw.trackingappbackend.dev;

import de.dhbw.trackingappbackend.entity.AppUser;
import de.dhbw.trackingappbackend.entity.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.UUID;

@Configuration
@Profile("dev")
public class DatabaseInitialData {

    @Bean
    public CommandLineRunner createTestData(UserRepository userRepository){
        return (args) -> {
            userRepository.save(new AppUser(UUID.randomUUID().toString(),"firstname1","lastname1", "test1@test.de","Passwort123."));
            userRepository.save(new AppUser(UUID.randomUUID().toString(),"firstname2","lastname2", "test2@test.de","Passwort123."));
            userRepository.save(new AppUser(UUID.randomUUID().toString(),"firstname3","lastname3", "test3@test.de","Passwort123."));
        };
    }

}
