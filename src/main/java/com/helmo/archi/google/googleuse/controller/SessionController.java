package com.helmo.archi.google.googleuse.controller;

import com.helmo.archi.google.googleuse.model.Session;
import com.helmo.archi.google.googleuse.service.SessionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sessions")
public class SessionController implements BasicController<Session> {
	
	private final SessionService sesSrv;
	
	public SessionController(SessionService sesSrv) {
		this.sesSrv = sesSrv;
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
	public List<Session> create(@RequestBody Session... ses) {
		return sesSrv.create(ses);
	}
	
	
	@Override
	@PutMapping
	@Secured("ROLE_USER")
	public List<Session> update(@RequestBody Session... ses) {
		return sesSrv.update(ses);
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
