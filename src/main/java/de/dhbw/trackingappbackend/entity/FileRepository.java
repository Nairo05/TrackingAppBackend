package de.dhbw.trackingappbackend.entity;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FileRepository extends MongoRepository<FileEntity, String> {

    Optional<FileEntity> findFirstByOwnerId(String ownerId);

    Optional<FileEntity> findFirstById(String id);

    List<FileEntity> findAllByOwnerId(String ownerId);
}
