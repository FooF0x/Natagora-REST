package com.helmo.archi.google.googleuse.controller;

import com.helmo.archi.google.googleuse.model.Bird;
import com.helmo.archi.google.googleuse.repository.BirdRepository;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/birds")
public class BirdController {

	private final BirdRepository brdSrv;
	
	public BirdController(BirdRepository brdSrv) {
		this.brdSrv = brdSrv;
	}
	
	@GetMapping
	@Secured("ROLE_ADMIN")
	public List<Bird> getBird() {
		return brdSrv.findAll();
	}
	
	@PostMapping
	@Secured("ROLE_ADMIN")
	public void postBird(Bird bird) {
		brdSrv.save(bird);
	}
}
