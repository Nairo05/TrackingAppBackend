package de.dhbw.trackingappbackend.entity;

import org.springframework.data.geo.Box;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface LocationRepository extends MongoRepository<Location, String> {

    List<Location> findByAppUserIdAndPositionWithin(String appUserId, Box b);
}
