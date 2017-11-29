package com.helmo.archi.google.googleuse.service;

import com.helmo.archi.google.googleuse.model.Session;
import com.helmo.archi.google.googleuse.repository.SessionRepository;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class SessionService implements BasicService<Session, Long> {

	private final SessionRepository sesRepo;


	public SessionService(SessionRepository sesRepo) {
		this.sesRepo = sesRepo;
	}

	@Override
	public List<Session> getAll() {
		return sesRepo.findAll();
	}
	
	@Override
	public Session getById(Long id) {
		return sesRepo.findOne(id);
	}
	
	@Override
	public Session create(Session ses) {
		Session two = new Session();
		two.setName(ses.getName());
		two.setUser(ses.getUser());
		two.setLatitude(ses.getLatitude());
		two.setLongitude(ses.getLongitude());
		two.setDateStart(new Timestamp(new Date().getTime())); //TODO To change
		two.setDateEnd(new Timestamp(new Date().getTime())); //TODO To change
		two.setObservations(ses.getObservations());
		
		return sesRepo.save(two);
	}
	
	@Override
	public Session update(Session ses) {
		return sesRepo.save(ses);
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
}
