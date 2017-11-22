package com.helmo.archi.google.googleuse.repository;

import com.helmo.archi.google.googleuse.model.BirdTwo;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BirdTwoRepository extends CrudRepository<BirdTwo, String> {
	
	List<BirdTwo> findAll();
	
}
