package com.helmo.archi.google.googleuse.service;

import com.helmo.archi.google.googleuse.model.Observation;
import com.helmo.archi.google.googleuse.repository.ObservationRepository;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Service
public class ObservationService {
	
	private final ObservationRepository obsRepo;
	
	public ObservationService(ObservationRepository obsRepo) {
		this.obsRepo = obsRepo;
	}
	
	public List<Observation> getObservations() {
		return obsRepo.findAll();
	}
	
	public void createObservation(Observation obs) {
		Observation two = new Observation();
		two.setDateTime(new Timestamp(new Date().getTime()));
		
		two.setLatitude(obs.getLatitude());
		two.setLongitude(obs.getLongitude());
		two.setAnalyseResult(obs.getAnalyseResult());
		two.setBird(obs.getBird());
		two.setMediaType(obs.getMediaType());
		two.setNbrObs(obs.getNbrObs());
		two.setOnlinePath(obs.getOnlinePath());
		two.setValidation(obs.isValidation());
		two.setSession(obs.getSession());
		
		obsRepo.save(two);
	}
}
