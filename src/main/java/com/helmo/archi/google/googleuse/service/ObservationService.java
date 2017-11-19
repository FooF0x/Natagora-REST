package com.helmo.archi.google.googleuse.service;

import com.helmo.archi.google.googleuse.model.Observation;
import com.helmo.archi.google.googleuse.repository.ObservationRepository;
import com.helmo.archi.google.googleuse.storage.BirdDatastore;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Service
public class ObservationService {
	
	private final ObservationRepository obsRepo;
	private final BirdDatastore birdDatastore;
	
	public ObservationService(ObservationRepository obsRepo, BirdDatastore birdDatastore) {
		this.obsRepo = obsRepo;
		this.birdDatastore = birdDatastore;
	}
	
	public List<Observation> getObservations() {
		List<Observation> rtn = obsRepo.findAll();
		for (Observation obs : obsRepo.findAll())
			obs.setBird(birdDatastore.find(obs.getBirdId()));
		
		return rtn;
	}
	
	public void createObservation(Observation obs) {
		Observation two = new Observation();
		two.setDateTime(new Timestamp(new Date().getTime()));
		
		two.setLatitude(obs.getLatitude());
		two.setLongitude(obs.getLongitude());
		two.setAnalyseResult(obs.getAnalyseResult());
		two.setBirdId(obs.getBirdId());
		two.setMediaType(obs.getMediaType());
		two.setNbrObs(obs.getNbrObs());
		two.setOnlinePath(obs.getOnlinePath());
		two.setValidation(obs.isValidation());
		two.setSession(obs.getSession());
		
		obsRepo.save(two);
	}
}
