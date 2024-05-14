package de.dhbw.trackingappbackend.entity;

import de.dhbw.trackingappbackend.entity.user.Achievement;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AchievementRepository extends MongoRepository<Achievement, String> {

    List<Achievement> findAchievementsByIdIn(List<String> achievementIds);
}
