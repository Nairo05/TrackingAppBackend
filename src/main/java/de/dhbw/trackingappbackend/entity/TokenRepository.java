package de.dhbw.trackingappbackend.entity;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TokenRepository extends MongoRepository<TokenEntity, String> {

    List<TokenEntity> findRefreshTokenEntityByUserId(String UserId);

    boolean deleteAllByUserId(String userId);
}



