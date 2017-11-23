package com.helmo.archi.google.googleuse.controller;

import com.helmo.archi.google.googleuse.model.Bird;
import com.helmo.archi.google.googleuse.service.BirdService;
import com.helmo.archi.google.googleuse.service.NextSequenceService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/birds")
public class BirdController {

	private final BirdService brdSrv;
	private final NextSequenceService nextSeq;
	
	public BirdController(BirdService brdSrv, NextSequenceService nxtSeq) {
		this.brdSrv = brdSrv;
		this.nextSeq = nxtSeq;
	}
	
	@GetMapping
	@Secured("ROLE_ADMIN")
	public List<Bird> getBird() {
		return brdSrv.findAll();
	}
	
	@PostMapping
	@Secured("ROLE_ADMIN")
	public void postBird(@RequestBody Bird bird) {
		bird.setId(nextSeq.getNextSequence("birds"));
		brdSrv.save(bird);
	}
}
