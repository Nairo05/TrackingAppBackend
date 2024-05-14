package de.dhbw.trackingappbackend.entity;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AchievementRepository extends MongoRepository<Achievement, String> {

    List<Achievement> findAchievementsByIdIn(List<String> achievementIds);

    List<Achievement> findAchievementsByIdNotIn(List<String> achievementIds);
}
