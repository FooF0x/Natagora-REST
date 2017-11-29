package com.helmo.archi.google.googleuse.service;

import com.helmo.archi.google.googleuse.model.Observation;
import com.helmo.archi.google.googleuse.repository.BirdRepository;
import com.helmo.archi.google.googleuse.repository.ObservationRepository;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Service
public class ObservationService implements BasicService<Observation, Long> {
	
	private final ObservationRepository obsRepo;
	private final BirdRepository brdRepo;
	
	public ObservationService(ObservationRepository obsRepo, BirdRepository birdDatastore) {
		this.obsRepo = obsRepo;
		this.brdRepo = birdDatastore;
	}
	
	@Override
	public List<Observation> getAll() {
		List<Observation> rtn = obsRepo.findAll();
		for (Observation obs : obsRepo.findAll())
			obs.setBird(brdRepo.findOne(obs.getBirdId()));
		
		return rtn;
	}
	
	@Override
	public Observation getById(Long id) {
		Observation obs = obsRepo.findOne(id);
		obs.setBird(brdRepo.findOne(obs.getBirdId()));
		return obs;
	}
	
	@Override
	public Observation create(Observation obs) {
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
		
		return obsRepo.save(two);
	}
	
	@Override
	public Observation update(Observation observation) {
		return null;
	}
	
	@Override
	public void deleteById(Long id) {
		obsRepo.delete(id);
	}
	
	@Override
	public void delete(Observation... observations) {
	}
	
	@Override
	public void delete(Observation observation) {
	}
}
