package com.helmo.archi.google.googleuse.storage;

import com.helmo.archi.google.googleuse.model.Bird;

import java.util.List;

public interface BirdDAO extends DAO<Bird, Long> {
//	void deleteBird(Bird birdId);
	
	List<Bird> findAll();
	Bird find(Long id);
	
	Long save(Bird entity);
	
	void update(Bird bird);
	
	void delete(Long id);
	
	boolean exist(Long id);
}
