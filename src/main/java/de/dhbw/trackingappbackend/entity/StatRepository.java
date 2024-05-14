package de.dhbw.trackingappbackend.entity;

import de.dhbw.trackingappbackend.entity.location.Stat;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StatRepository extends MongoRepository<Stat, String> {

    List<Stat> findStatsByIdIn(List<String> statIds);

    Stat findStatByIdInAndLaenderKuerzel(List<String> statIds, String laenderKuerzel);
}
