package com.helmo.archi.google.googleuse.controller;

import com.helmo.archi.google.googleuse.model.Session;
import com.helmo.archi.google.googleuse.service.SessionService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sessions")
public class SessionController {
	
	private final SessionService sesSrv;
	
	public SessionController(SessionService sesSrv) {
		this.sesSrv = sesSrv;
	}
	
	@GetMapping()
	@Secured("ROLE_USER")
	public List<Session> getSessions() {
		return sesSrv.getAll();
	}
	
	@GetMapping("/{id}")
	@Secured("ROLE_USER")
	public Session getSessionById(@PathVariable("id") long id) {
		return sesSrv.getById(id);
	}
	
	@PostMapping
	@Secured("ROLE_USER")
	public void createSession(@RequestBody Session ses) {
		sesSrv.create(ses);
	}
	
	@PutMapping
	@Secured("ROLE_USER")
	public void update(@RequestBody Session ses) {
		sesSrv.update(ses);
	}
}
