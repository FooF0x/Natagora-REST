package com.helmo.archi.google.googleuse.repository;

import com.helmo.archi.google.googleuse.model.Bird;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface BirdRepository extends MongoRepository<Bird, Long> {
	
	List<Bird> findAll();
	
}
