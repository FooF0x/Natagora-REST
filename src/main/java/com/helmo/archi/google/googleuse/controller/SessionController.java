package com.helmo.archi.google.googleuse.controller;

import com.helmo.archi.google.googleuse.model.Session;
import com.helmo.archi.google.googleuse.service.SessionService;
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
	public List<Session> getSessions() {
		return sesSrv.getSessions();
	}
	
	@PostMapping
	public void createSession(@RequestBody Session ses) {
		sesSrv.createSession(ses);
	}
}
