package com.helmo.archi.google.googleuse.service;

import com.helmo.archi.google.googleuse.model.Bird;
import com.helmo.archi.google.googleuse.repository.BirdRepository;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class BirdService implements BasicService<Bird, Long> {
	
	private final BirdRepository brdRepo;
	
	public BirdService(BirdRepository brdRepo) {
		this.brdRepo = brdRepo;
	}
	
	@Override
	public List<Bird> getAll() {
		return brdRepo.findAll();
	}
	
	@Override
	public Bird getById(Long id) {
		return brdRepo.findOne(id);
	}
	
	@Override
	public Bird create(Bird bird) {
		return brdRepo.insert(bird);
	}
	
	public Bird update(Bird bird) {
		brdRepo.delete(bird.getId());
		return brdRepo.insert(bird);
	}
	
	@Override
	public void deleteById(Long id) {
		brdRepo.delete(id);
	}
	
	@Override
	public void delete(Bird... birds) {
		brdRepo.delete(Arrays.asList(birds));
	}
	
	@Override
	public void delete(Bird bird) {
		brdRepo.delete(bird);
	}
	
	public List<Bird> findSingleByArgs(String key, String item) {
		return null;
	}
	
	public List<Bird> findSingleByArgs(String key, double item) {
		return null;
	}
	
	public List<Bird> findSingleByArgs(String key, long item) {
		return null;
	}

//	public <X extends java.lang> List<Bird> findSingleByArgs(String key, X item) {
//		return null;
//	}

//	public <X extends java.lang> List<Bird> findSingleByArgs(String key, X x) {
//		return null;
//	}
}
