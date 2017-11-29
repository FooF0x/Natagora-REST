package com.helmo.archi.google.googleuse.service;

import com.helmo.archi.google.googleuse.model.Session;
import com.helmo.archi.google.googleuse.repository.SessionRepository;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
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
	public List<Session> create(Session... sessions) {
		List<Session> rtn = new ArrayList<>();
		for(Session ses : sessions)
			rtn.add(create(ses));
		return rtn;
	}
	
	public Session getById(long id) {
		return sesRepo.findOne(id);
	}
	
	public void update(Session ses) {
		sesRepo.save(ses);
	}
}
