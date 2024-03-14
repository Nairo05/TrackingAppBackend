package de.dhbw.trackingappbackend.entity;

import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<AppUser, Long> {

    AppUser findById(long id);

}
