package com.helmo.archi.google.googleuse.controller;

import com.helmo.archi.google.googleuse.model.Observation;
import com.helmo.archi.google.googleuse.model.Session;
import com.helmo.archi.google.googleuse.service.SessionService;
import com.helmo.archi.google.googleuse.tools.ObservationChecker;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/sessions")
public class SessionController implements BasicController<Session> {
	
	private final SessionService sesSrv;
	private final ObservationChecker obsChecker;
	
	public SessionController(SessionService sesSrv, ObservationChecker obsChecker) {
		this.sesSrv = sesSrv;
		this.obsChecker = obsChecker;
	}
	
	@Override
	@GetMapping()
	@Secured("ROLE_USER")
	public List<Session> getAll() {
		return sesSrv.getAll();
	}
	
	@Override
	@GetMapping("/{id}")
	@Secured("ROLE_USER")
	public Session getOne(@PathVariable("id") long id) {
		return sesSrv.getById(id);
	}
	
	@Override
	@PostMapping
	@Secured("ROLE_USER")
	public ResponseEntity create(@RequestBody Session... sessions) {
		try {
			List<Session> rtn = new ArrayList<>();
			//TODO Analyse inside obs
			for (Session ses : sessions) {
				Observation[] obs = ses.getObservations().toArray(new Observation[ses.getObservations().size()]);
				ses.setObservations(new ArrayList<>());
				Session added = sesSrv.create(ses);
				added.setObservations(
					  obsChecker.observationAdder(added, obs));
				rtn.add(added);
			}
			return ResponseEntity.ok(rtn);
		} catch (Exception ex) {
			return ResponseEntity.badRequest().build();
		}
	}
	
	@Override
	@PutMapping
	@Secured("ROLE_USER")
	public ResponseEntity update(@RequestBody Session... ses) {
		try {
			return ResponseEntity.ok(sesSrv.update(ses));
		} catch (Exception ex) {
			return ResponseEntity.badRequest().build();
		}
	}
	
	@Override
	public ResponseEntity deleteOne(long id) {
		sesSrv.deleteById(id);
		return ResponseEntity.ok(null);
	}
	
	@Override
	public ResponseEntity delete(Session... sessions) {
		sesSrv.delete(sessions);
		return ResponseEntity.ok(null);
	}
}
