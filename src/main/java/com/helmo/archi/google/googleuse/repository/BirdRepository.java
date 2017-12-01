package com.helmo.archi.google.googleuse.repository;

import com.helmo.archi.google.googleuse.model.Bird;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface BirdRepository extends MongoRepository<Bird, Long> {
	@Query("{ ?0 : [?1] }")
	List<Bird> findAllByArgs(String key, String args);
}
