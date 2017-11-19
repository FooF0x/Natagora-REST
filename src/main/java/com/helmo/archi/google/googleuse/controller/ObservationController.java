package com.helmo.archi.google.googleuse.controller;

import com.helmo.archi.google.googleuse.model.Observation;
import com.helmo.archi.google.googleuse.service.ObservationService;
import com.helmo.archi.google.googleuse.storage.GoogleStorage;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/observations")
public class ObservationController {

	private final ObservationService obsSrv;
	private final GoogleStorage storage;
	
	public ObservationController(ObservationService obsSrv, GoogleStorage storage) {
		this.obsSrv = obsSrv;
		this.storage = storage;
	}
	
	@GetMapping
	public List<Observation> getObservations() {
		return obsSrv.getObservations();
	}
	
	@PostMapping
	public void createObservation(@RequestBody Observation obs) {
		obsSrv.createObservation(obs);
	}
	
	@DeleteMapping("/{id}")
	public void deleteObservation(@PathVariable("id") Long id) {
		obsSrv.deleteById(id);
	}
}
