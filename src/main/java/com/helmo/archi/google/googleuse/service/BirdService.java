package com.helmo.archi.google.googleuse.service;

import com.helmo.archi.google.googleuse.model.Bird;
import com.helmo.archi.google.googleuse.storage.GoogleDatastore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BirdService {

	private final GoogleDatastore datastore = new GoogleDatastore();
	
	public void createBird(Bird bird) {
		datastore.createBird(bird);
	}
	
	public List<Bird> getBirds() {
		return datastore.findAll();
	}
}
	

