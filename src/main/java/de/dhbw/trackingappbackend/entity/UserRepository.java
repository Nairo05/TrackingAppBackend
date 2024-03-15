package de.dhbw.trackingappbackend.entity;


import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<AppUser, String> {

    Optional<AppUser> findByEmail(String email);

    Boolean existsByEmail(String email);

}
