package com.helmo.archi.google.googleuse.service;

import com.helmo.archi.google.googleuse.model.Observation;
import com.helmo.archi.google.googleuse.model.Session;
import com.helmo.archi.google.googleuse.repository.BirdRepository;
import com.helmo.archi.google.googleuse.repository.ObservationRepository;
import com.helmo.archi.google.googleuse.tools.Time;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ObservationService implements BasicService<Observation, Long> {
//	private Map<Long, Bird> birdsCache; //TODO Update Cache
	
	private final ObservationRepository obsRepo;
	private final BirdRepository brdRepo;
	
	public ObservationService(ObservationRepository obsRepo, BirdRepository birdRepository) {
		this.obsRepo = obsRepo;
		this.brdRepo = birdRepository;
//		birdsCache = new TreeMap<>();
//		brdRepo.findAll().forEach(b -> birdsCache.put(b.getId(), b));
	}
	
	@Override
	public List<Observation> getAll() {
		List<Observation> rtn = obsRepo.findAll();
		rtn.forEach(obs -> obs.setBird(brdRepo.findOne(obs.getBirdId())));
//		rtn.forEach(obs -> obs.setBird(birdsCache.get(obs.getBirdId())));
		return rtn;
	}
	
	@Override
	public Observation getById(Long id) {
		Observation obs = obsRepo.findOne(id);
		obs.setBird(brdRepo.findOne(obs.getBirdId()));
//		obs.setBird(birdsCache.get(obs.getBirdId()));
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
		two.setPublicLink(
			  obs.getPublicLink() != null
					? obs.getPublicLink()
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
		original.setPublicLink(
			  obs.getPublicLink() != null
					? obs.getPublicLink()
					: original.getPublicLink());
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
	
	public List<Observation> getRange(long one, long two) {
		return getAll()
			  .stream()
			  .skip(one)
			  .limit(two - one)
			  .collect(Collectors.toList());
	}
	
	public List<Observation> getBySession(Session ses) {
		List<Observation> observations = obsRepo.getBySession(ses);
		observations.forEach(obs -> obs.setBird(brdRepo.findOne(obs.getBirdId())));
//		observations.forEach(obs -> obs.setBird(birdsCache.get(obs.getBirdId())));
		return observations;
	}
}
