package com.helmo.archi.google.googleuse.repository;

import com.helmo.archi.google.googleuse.model.Bird;
import com.helmo.archi.google.googleuse.storage.BirdDatastore;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BirdRepository implements BirdRepositoryInterface {
	
	private final BirdDatastore datastore;
	
	public BirdRepository(BirdDatastore datastore) {
		this.datastore = datastore;
	}
	
	@Override
	public Long save(Bird entity) {
		return datastore.save(entity);
	}
	
	@Override
	public List<Bird> findAll() {
		return datastore.findAll();
	}
	
	@Override
	public Bird find(Long key) {
		return datastore.find(key);
	}
	
	@Override
	public void update(Bird entity) {
		datastore.update(entity);
	}
	
	@Override
	public void delete(Long id) {
		datastore.delete(id);
	}
	
	@Override
	public boolean exist(Long id) {
		return datastore.exist(id);
	}

//	Bird getById(long id);
}
