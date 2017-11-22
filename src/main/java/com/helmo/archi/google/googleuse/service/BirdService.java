package com.helmo.archi.google.googleuse.service;

import com.helmo.archi.google.googleuse.model.BirdTwo;
import com.helmo.archi.google.googleuse.repository.BirdTwoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BirdTwoService {

	private final BirdTwoRepository brdRepo;
	
	public BirdTwoService(BirdTwoRepository brdRepo) {
		this.brdRepo = brdRepo;
	}
	
	
	public List<BirdTwo> findAll() {
		return (List<BirdTwo>) brdRepo.findAll();
	}
	
	public void create(BirdTwo bird) {
		brdRepo.save(bird);
	}
}
