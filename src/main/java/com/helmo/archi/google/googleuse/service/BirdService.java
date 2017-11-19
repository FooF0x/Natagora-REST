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
	
	public void createBird(Bird bird) {
		brdRepo.save(bird);
	}
	
	public List<Bird> getBirds() {
		return brdRepo.findAll();
	}
	
	public Bird getById(long id) {
		return brdRepo.find(id);
	}
	
	public void update(Bird bird) {
		brdRepo.update(bird);
	}
	
	public void deleteBird(Long id) {
		brdRepo.delete(id);
	}
}
	

