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
		return (List<Bird>) brdRepo.findAll();
	}
	
	public void create(Bird bird) {
		brdRepo.save(bird);
	}
}
