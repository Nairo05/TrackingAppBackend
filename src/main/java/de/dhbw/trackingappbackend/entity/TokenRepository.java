package de.dhbw.trackingappbackend.entity;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends MongoRepository<TokenEntity, String> {

    List<TokenEntity> findRefreshTokenEntityByUserId(String UserId);

    void deleteAllByUserId(String userId);

    Optional<TokenEntity> findByRefreshToken(String refreshToken);
}



