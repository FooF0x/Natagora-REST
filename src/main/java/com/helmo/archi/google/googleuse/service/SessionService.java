package com.helmo.archi.google.googleuse.service;

import com.helmo.archi.google.googleuse.model.Bird;
import com.helmo.archi.google.googleuse.model.Observation;
import com.helmo.archi.google.googleuse.model.Session;
import com.helmo.archi.google.googleuse.model.User;
import com.helmo.archi.google.googleuse.repository.BirdRepository;
import com.helmo.archi.google.googleuse.repository.ObservationRepository;
import com.helmo.archi.google.googleuse.repository.SessionRepository;
import com.helmo.archi.google.googleuse.tools.Time;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SessionService implements BasicService<Session, Long> {
	
	private final SessionRepository sesRepo;
	private final ObservationRepository obsRepo;
	private final BirdRepository brdRepo;

//	private List<Bird> birds; //Cache list to avoid multiple MongoDB Call //TODO Update the list when a bird is added
	
	public SessionService(SessionRepository sesRepo, ObservationRepository obsRepo, BirdRepository brdRepo) {
		this.sesRepo = sesRepo;
		this.obsRepo = obsRepo;
		this.brdRepo = brdRepo;
	}
	
	@Override
	public List<Session> getAll() {
		List<Session> sessions = sesRepo.findAll();
		List<Bird> birds = brdRepo.findAll();
		sessions.forEach(
			  s -> setObservations(s, birds));
		return sessions;
	}
	
	@Override
	public Session getById(Long id) {
		Session ses = sesRepo.findOne(id);
		setObservations(ses, null);
		return ses;
	}
	
	private void setObservations(Session ses, List<Bird> birds) {
		if (birds == null) brdRepo.findAll();
		List<Observation> temp = obsRepo.getBySession(ses);
		temp.forEach(
			  o -> o.setBird(birds.stream()
					.filter(b -> b.getId() == o.getBirdId())
					.findFirst().get())
		);
		ses.setObservations(temp);
	}
	
	@Override
	public Session create(Session ses) {
		Session two = new Session();
		two.setName(ses.getName());
		two.setUser(ses.getUser());
		two.setLatitude(ses.getLatitude());
		two.setLongitude(ses.getLongitude());
		two.setObservations(ses.getObservations());
		two.setTemperature(ses.getTemperature());
		two.setWind(ses.getWind());
		two.setRain(ses.getRain());
		two.setDateStart(
			  (ses.getDateStart() != null)
					? ses.getDateStart()
					: Time.getTime()
		);
		two.setDateEnd(
			  (ses.getDateEnd() != null)
					? ses.getDateEnd()
					: Time.getTime()
		);
		
		return sesRepo.save(two);
	}
	
	@Override
	public Session update(Session ses) {
		Session original = sesRepo.findOne(ses.getId());
		
		original.setName(
			  (ses.getName() != null)
					? ses.getName()
					: original.getName()
		);
		original.setLatitude(
			  (ses.getLatitude() != null)
					? ses.getLatitude()
					: original.getLatitude()
		);
		original.setLongitude(
			  (ses.getLongitude() != null)
					? ses.getLongitude()
					: original.getLongitude()
		);
		original.setDateStart(
			  (ses.getDateStart() != null)
					? ses.getDateStart()
					: original.getDateStart()
		);
		original.setDateEnd(
			  (ses.getDateEnd() != null)
					? ses.getDateEnd()
					: original.getDateEnd()
		);
		original.setObservations(
			  (ses.getObservations() != null)
					? ses.getObservations()
					: original.getObservations()
		);
		original.setUser(
			  (ses.getUser() != null)
					? ses.getUser()
					: original.getUser()
		);
		
		return sesRepo.save(original);
	}
	
	@Override
	public void delete(Session... sessions) {
		sesRepo.delete(Arrays.asList(sessions));
	}
	
	@Override
	public void delete(Session session) {
		sesRepo.delete(session);
	}
	
	@Override
	public void deleteById(Long id) {
		sesRepo.delete(id);
	}
	
	public List<Session> findByUser(User user) {
		List<Session> sessions = sesRepo.findByUser(user);
		sessions.forEach(
			  s -> setObservations(s, null)
		);
		return sessions;
	}
	
	public List<Session> findByUserId(Long id) {
		List<Session> sessions = sesRepo.findByUser_Id(id);
		sessions.forEach(
			  s -> setObservations(s, null)
		);
		return sessions;
	}
	
	
	public List<Session> getRange(long one, long two) {
		return getAll()
			  .stream()
			  .skip(one)
			  .limit(two - one)
			  .collect(Collectors.toList());
		
	}
}
