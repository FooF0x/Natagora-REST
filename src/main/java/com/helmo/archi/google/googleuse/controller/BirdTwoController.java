package com.helmo.archi.google.googleuse.controller;

import com.helmo.archi.google.googleuse.model.BirdTwo;
import com.helmo.archi.google.googleuse.repository.BirdTwoRepository;
import com.helmo.archi.google.googleuse.service.BirdTwoService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/brd")
public class BirdTwoController {

	private final BirdTwoRepository brdSrv;
	
	public BirdTwoController(BirdTwoRepository brdSrv) {
		this.brdSrv = brdSrv;
	}
	
	@GetMapping
	@Secured("ROLE_ADMIN")
	public List<BirdTwo> getBird() {
		return brdSrv.findAll();
	}
	
	@PostMapping
	@Secured("ROLE_ADMIN")
	public void postBird(BirdTwo bird) {
		brdSrv.save(bird);
	}
}
