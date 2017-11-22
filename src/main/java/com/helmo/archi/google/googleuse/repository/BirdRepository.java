package com.helmo.archi.google.googleuse.repository;

import com.helmo.archi.google.googleuse.model.Bird;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BirdRepository extends CrudRepository<Bird, Long> {
	
	List<Bird> findAll();
	
}
