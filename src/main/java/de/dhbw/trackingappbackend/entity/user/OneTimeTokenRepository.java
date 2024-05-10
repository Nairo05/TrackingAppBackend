package de.dhbw.trackingappbackend.entity.user;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OneTimeTokenRepository extends MongoRepository<OneTimeTokenEntity, String> {

    Optional<OneTimeTokenEntity> findByEmail(String email);

    boolean existsByEmail(String email);

    OneTimeTokenEntity getOneTimeTokenEntityByEmailAndPin(String email, int pin);

}
