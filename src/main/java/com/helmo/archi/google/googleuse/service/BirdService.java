package com.helmo.archi.google.googleuse.service;

import com.helmo.archi.google.googleuse.model.Bird;
import com.helmo.archi.google.googleuse.repository.BirdRepository;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class BirdService implements BasicService<Bird, Long> {
	
	private final BirdRepository brdRepo;
	private final NextSequenceService nextSeq;
	
	public BirdService(BirdRepository brdRepo, NextSequenceService nextSeq) {
		this.brdRepo = brdRepo;
		this.nextSeq = nextSeq;
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
	public Bird create(Bird toSave) {
		Bird bird = toSave.getAddable();
		bird.setId(nextSeq.getNextSequence("birds"));
		return brdRepo.insert(bird);
	}
	
	public Bird update(Bird toUpdate) {
		Bird bird = brdRepo.findOne(toUpdate.getId());
		
		bird.setName(toUpdate.getName() != null
			  ? toUpdate.getName()
			  : bird.getName());
		bird.setDescription(toUpdate.getDescription() != null
			  ? toUpdate.getDescription()
			  : bird.getDescription());
		bird.setData(toUpdate.getData() != null
			  ? toUpdate.getData()
			  : bird.getData());
		bird.setPicture(toUpdate.getPicture() != null
			  ? toUpdate.getPicture()
			  : bird.getPicture());
		
		brdRepo.delete(toUpdate.getId());
		return brdRepo.insert(toUpdate);
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
