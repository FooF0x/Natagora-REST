package com.helmo.archi.google.googleuse.service;

import com.helmo.archi.google.googleuse.model.Bird;
import com.helmo.archi.google.googleuse.repository.BirdRepository;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class BirdService implements BasicService<Bird, Long> {
	
	private final BirdRepository brdRepo;
	private final NextSequenceService nextSeq;
	private final MongoTemplate monqoTemplate;
	
	public BirdService(BirdRepository brdRepo, NextSequenceService nextSeq, MongoTemplate monqoTemplate) {
		this.brdRepo = brdRepo;
		this.nextSeq = nextSeq;
		this.monqoTemplate = monqoTemplate;
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
		Bird bird = new Bird();
		bird.setName(toSave.getName());
		bird.setDescription(toSave.getDescription());
		bird.setData(toSave.getData());
		bird.setPicture(toSave.getPicture());
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
	
	public List<Bird> findAllByArgs(String key, String args) {
		Query findQuery = new Query();
		Criteria findCriteria =  Criteria.where("data." + key).is(args);
		findQuery.addCriteria(findCriteria);
		List<Bird> birds = monqoTemplate.find(findQuery, Bird.class);
		
		return birds;
//		List<Team> teams = mongoTemplate.find(findQuery, Team.class);
	}
	
	public List<Bird> findSingleByArgs(String key, String item) {
		return findAllByArgs(key, item);
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
