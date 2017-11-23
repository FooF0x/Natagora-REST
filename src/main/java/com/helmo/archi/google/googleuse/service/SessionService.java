package com.helmo.archi.google.googleuse.service;

import com.helmo.archi.google.googleuse.model.Session;
import com.helmo.archi.google.googleuse.repository.SessionRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.sql.Timestamp;
import java.util.List;

@Service
public class SessionService {

	private final SessionRepository sesRepo;


	public SessionService(SessionRepository sesRepo) {
		this.sesRepo = sesRepo;
	}

	public List<Session> getSessions() {
		return sesRepo.findAll();
	}
	
	public void createSession(Session ses) {
		Session two = new Session();
		two.setName(ses.getName());
		two.setUser(ses.getUser());
		two.setLatitude(ses.getLatitude());
		two.setLongitude(ses.getLongitude());
		two.setDateStart(new Timestamp(new Date().getTime())); //TODO To change
		two.setDateEnd(new Timestamp(new Date().getTime())); //TODO To change
		two.setObservations(ses.getObservations());
		
		sesRepo.save(two);
	}
	
	public Session getById(long id) {
		return sesRepo.findOne(id);
	}
	
	public void update(Session ses) {
		sesRepo.save(ses);
	}
}
