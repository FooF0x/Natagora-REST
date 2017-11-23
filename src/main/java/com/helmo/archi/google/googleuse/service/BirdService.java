package com.helmo.archi.google.googleuse.service;

import com.helmo.archi.google.googleuse.model.Bird;
import com.helmo.archi.google.googleuse.repository.BirdRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BirdService {

	private final BirdRepository brdRepo;
	
	public BirdService(BirdRepository brdRepo) {
		this.brdRepo = brdRepo;
	}
	
	public List<Bird> findAll() {
		return brdRepo.findAll();
	}
	
	public void save(Bird bird) {
		brdRepo.insert(bird);
	}
	
	public void update(Bird bird) {
		brdRepo.delete(bird.getId());
		brdRepo.insert(bird);
	}
	
	public void delete(long id) {
		brdRepo.delete(id);
	}
	
	public Bird find(long id) {
		return brdRepo.findOne(id);
	}
}
