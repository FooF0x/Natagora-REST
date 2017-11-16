package com.helmo.archi.google.googleuse.storage;

import com.helmo.archi.google.googleuse.model.Bird;

import java.util.List;

public interface BirdDAO {
	Long createBird(Bird bird);
	Bird readBird(Long idBird);
	void updateBird(Bird bird);
	void deleteBird(Bird bird);
	
	List<Bird> findAll();
}
