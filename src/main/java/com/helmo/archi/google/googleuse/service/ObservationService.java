package com.helmo.archi.google.googleuse.service;

import com.helmo.archi.google.googleuse.model.Observation;
import com.helmo.archi.google.googleuse.repository.BirdRepository;
import com.helmo.archi.google.googleuse.repository.ObservationRepository;
import com.helmo.archi.google.googleuse.tools.Time;
import org.springframework.stereotype.Service;

import java.util.Arrays;
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
		
		two.setLatitude(obs.getLatitude());
		two.setLongitude(obs.getLongitude());
		two.setBirdId(obs.getBirdId());
		two.setMediaType(obs.getMediaType());
		two.setNbrObs(obs.getNbrObs());
		two.setDateTime(
			  obs.getDateTime() != null
					? obs.getDateTime()
					: Time.getTime());
		two.setAnalyseResult(
			  obs.getAnalyseResult() != null
					? obs.getAnalyseResult()
					: "");
		two.setOnlinePath(
			  obs.getOnlinePath() != null
					? obs.getOnlinePath()
					: "");
		two.setValidation(obs.isValidation());
		two.setSession(obs.getSession());
		
		return obsRepo.save(two);
	}
	
	@Override
	public Observation update(Observation obs) {
		Observation original = obsRepo.findOne(obs.getId());
		
		original.setBirdId(obs.getBirdId());
		original.setNbrObs(obs.getNbrObs());
		original.setValidation(obs.isValidation());
		original.setLongitude(
			  obs.getLongitude() != null
					? obs.getLongitude()
					: original.getLongitude());
		original.setLatitude(
			  obs.getLatitude() != null
					? obs.getLatitude()
					: original.getLatitude());
		original.setDateTime(
			  obs.getDateTime() != null
					? obs.getDateTime()
					: original.getDateTime());
		original.setOnlinePath(
			  obs.getOnlinePath() != null
					? obs.getOnlinePath()
					: original.getOnlinePath());
		original.setAnalyseResult(
			  obs.getAnalyseResult() != null
					? obs.getAnalyseResult()
					: original.getAnalyseResult());
		original.setBird(
			  obs.getBird() != null
					? obs.getBird()
					: original.getBird());
		original.setMediaType(
			  obs.getMediaType() != null
					? obs.getMediaType()
					: original.getMediaType());
		original.setSession(
			  obs.getSession() != null
					? obs.getSession()
					: original.getSession());
		return obsRepo.save(original);
	}
	
	@Override
	public void deleteById(Long id) {
		obsRepo.delete(id);
	}
	
	@Override
	public void delete(Observation... observations) {
		obsRepo.delete(Arrays.asList(observations));
	}
	
	@Override
	public void delete(Observation observation) {
		obsRepo.delete(observation);
	}
}
