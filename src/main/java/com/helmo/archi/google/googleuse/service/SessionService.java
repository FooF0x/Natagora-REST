package com.helmo.archi.google.googleuse.service;

import com.helmo.archi.google.googleuse.model.Session;
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
	
	public SessionService(SessionRepository sesRepo, ObservationRepository obsRepo) {
		this.sesRepo = sesRepo;
		this.obsRepo = obsRepo;
	}
	
	@Override
	public List<Session> getAll() {
		List<Session> sessions = sesRepo.findAll();
		sessions.forEach(
			  s -> s.setObservations(obsRepo.getBySession(s)));
		return sessions;
	}
	
	@Override
	public Session getById(Long id) {
		Session ses = sesRepo.findOne(id);
		ses.setObservations(obsRepo.getBySession(ses));
		return ses;
	}
	
	@Override
	public Session create(Session ses) {
		Session two = new Session();
		two.setName(ses.getName());
		two.setUser(ses.getUser());
		two.setLatitude(ses.getLatitude());
		two.setLongitude(ses.getLongitude());
		two.setObservations(ses.getObservations());
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
	
	
	public List<Session> getRange(long one, long two) {
		return getAll()
			  .stream()
			  .skip(one)
			  .limit(two - one)
			  .collect(Collectors.toList());
		
	}
}
