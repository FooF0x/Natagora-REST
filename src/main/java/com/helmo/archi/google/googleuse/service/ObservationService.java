package com.helmo.archi.google.googleuse.service;

import com.helmo.archi.google.googleuse.model.Observation;
import com.helmo.archi.google.googleuse.model.Session;
import com.helmo.archi.google.googleuse.repository.*;
import com.helmo.archi.google.googleuse.tools.Time;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
public class ObservationService implements AccessRange<Observation, Long> {
//	private Map<Long, Bird> birdsCache; //TODO Update Cache
	
	private final ObservationRepository obsRepo;
	private final BirdRepository brdRepo;
	private final ReportRepository repRepo;
	private final CommentRepository cmtRepo;
	private final NotificationRepository notRepo;
	
	public ObservationService(ObservationRepository obsRepo, BirdRepository birdRepository, ReportRepository repRepo,
	                          CommentRepository cmtRepo, NotificationRepository notRepo) {
		this.obsRepo = obsRepo;
		this.brdRepo = birdRepository;
//		birdsCache = new TreeMap<>();
//		brdRepo.findAll().forEach(b -> birdsCache.put(b.getId(), b));
		this.repRepo = repRepo;
		this.cmtRepo = cmtRepo;
		this.notRepo = notRepo;
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
		deleteDependence(id);
		obsRepo.delete(id);
	}
	
	@Override
	public void delete(Observation... observations) {
		List<Observation> observationList = Arrays.asList(observations);
		observationList.forEach(
			  this::deleteDependence
		);
		obsRepo.delete(observationList);
	}
	
	@Override
	public void delete(Observation observation) {
		deleteDependence(observation);
		obsRepo.delete(observation);
	}
	
	private void deleteDependence(Observation obs) {
		cmtRepo.deleteAllByObservation(obs);
		notRepo.deleteAllByObservation(obs);
		repRepo.deleteAllByObservation(obs);
	}
	
	private void deleteDependence(long id) {
		cmtRepo.deleteAllByObservation_Id(id);
		notRepo.deleteAllByObservation_Id(id);
		repRepo.deleteAllByObservation_Id(id);
	}
	
	public List<Observation> getBySession(Session ses) {
		List<Observation> observations = obsRepo.getBySession(ses);
		observations.forEach(obs -> obs.setBird(brdRepo.findOne(obs.getBirdId())));
//		observations.forEach(obs -> obs.setBird(birdsCache.get(obs.getBirdId())));
		return observations;
	}
	
	public List<Observation> getByBirdId(long id) {
		return obsRepo.getByBirdId(id);
	}
}
