package de.dhbw.trackingappbackend.entity;


import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<AppUser, String> {

    Optional<AppUser> findByEmail(String email);

    Boolean existsByEmail(String email);

    Boolean existsByIdAndLocationIdsContains(String appUserId, String locationId);
}
