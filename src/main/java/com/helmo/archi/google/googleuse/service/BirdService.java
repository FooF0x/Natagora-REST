package com.helmo.archi.google.googleuse.service;

import com.helmo.archi.google.googleuse.model.Bird;
import com.helmo.archi.google.googleuse.model.Observation;
import com.helmo.archi.google.googleuse.repository.BirdRepository;
import com.helmo.archi.google.googleuse.repository.ObservationRepository;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class BirdService implements AccessRange<Bird, Long> {
	
	private final BirdRepository brdRepo;
	private final ObservationRepository obsRepo;
	
	private final NextSequenceService nextSeq;
	private final MongoTemplate mongoTemplate;
	
	private final String COLLECTION_NAME;
	private final Bird DEFAULT_BIRD;
	
	public BirdService(BirdRepository brdRepo, ObservationRepository obsSrv, NextSequenceService nextSeq,
	                   MongoTemplate mongoTemplate, Environment env) {
		this.brdRepo = brdRepo;
		this.obsRepo = obsSrv;
		this.nextSeq = nextSeq;
		this.mongoTemplate = mongoTemplate;
		this.COLLECTION_NAME = env.getProperty("mongodb.birds");
		
		DEFAULT_BIRD = brdRepo.findOne(0L);
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
		bird.setId(nextSeq.getNextSequence(COLLECTION_NAME));
		bird.setName(toSave.getName());
		bird.setDescription(toSave.getDescription());
		bird.setData(toSave.getData());
		bird.setPicture(toSave.getPicture());
		return brdRepo.insert(bird);
	}
	
	public Bird update(Bird toUpdate) {
		if (toUpdate.equals(DEFAULT_BIRD)) throw new IllegalArgumentException("Can't update this bird");
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
		return brdRepo.insert(bird);
	}
	
	@Override
	public void deleteById(Long id) {
		if(id == 0) throw new IllegalArgumentException("Can't delete this bird");
		manageObservationWhenDelete(id);
		brdRepo.delete(id);
	}
	
	@Override
	public void delete(Bird... birds) {
		List<Bird> arraysBird = Arrays.asList(birds);
		if(arraysBird.contains(DEFAULT_BIRD)) throw new IllegalArgumentException("Can't delete this bird");
		Arrays.asList(birds).forEach(this::manageObservationWhenDelete);
		brdRepo.delete(arraysBird);
	}
	
	@Override
	public void delete(Bird bird) {
		if(bird.equals(DEFAULT_BIRD)) throw new IllegalArgumentException("Can't delete this bird");
		manageObservationWhenDelete(bird);
		brdRepo.delete(bird);
	}
	
	private void manageObservationWhenDelete(long id) {
		List<Observation> observations = obsRepo.getByBirdId(id);
		observations.forEach(
			  o -> o.setBird(DEFAULT_BIRD)
		);
		obsRepo.save(observations);
	}
	
	private void manageObservationWhenDelete(Bird bird) {
		manageObservationWhenDelete(bird.getId());
	}
	
	public List<Bird> findSingleByArgs(String key, String item) {
		return findAllByArgs(key, item);
	}
	
	public List<Bird> findSingleByArgs(String key, double item) {
		return findAllByArgs(key, item);
	}
	
	public List<Bird> findSingleByArgs(String key, long item) {
		return findAllByArgs(key, item);
	}
	
	public List<Bird> findAllByArgs(String key, String args) {
		Query findQuery = new Query();
		findQuery.addCriteria(Criteria.where("data." + key).is(args));
		return mongoTemplate.find(findQuery, Bird.class);
	}
	
	public List<Bird> findAllByArgs(String key, double args) {
		Query findQuery = new Query();
		findQuery.addCriteria(Criteria.where("data." + key)
			  .gte(args - args * 0.5)
			  .lt(args + args * 1.5));
		return mongoTemplate.find(findQuery, Bird.class);
	}
	
	public boolean exist(long id) {
		return brdRepo.exists(id);
	}
}
