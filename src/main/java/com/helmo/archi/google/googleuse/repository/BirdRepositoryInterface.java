package com.helmo.archi.google.googleuse.repository;

import com.helmo.archi.google.googleuse.model.Bird;
import com.helmo.archi.google.googleuse.storage.BirdDAO;

import java.util.List;

public interface BirdRepositoryInterface extends BirdDAO {
	Long save(Bird entity);
	
	List<Bird> findAll();
	Bird find(Long key);
}
