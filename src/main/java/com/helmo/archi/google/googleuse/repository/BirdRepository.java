package com.helmo.archi.google.googleuse.repository;

import com.helmo.archi.google.googleuse.model.Bird;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BirdRepository extends MongoRepository<Bird, Long> {

}
