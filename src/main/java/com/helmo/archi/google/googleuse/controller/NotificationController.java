package com.helmo.archi.google.googleuse.controller;

import com.helmo.archi.google.googleuse.model.Notification;
import com.helmo.archi.google.googleuse.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notifications")
public class NotificationController implements BasicController<Notification> {
	
	private final NotificationService notSrv;
	
	public NotificationController(NotificationService notSrv) {
		this.notSrv = notSrv;
	}
	
	@Override
	@GetMapping
	@Secured("ROLE_USER")
	public List<Notification> getAll() {
		return notSrv.getAll();
	}
	
	@Override
	@GetMapping("/{id}")
	public Notification getOne(@PathVariable("id") long id) {
		return notSrv.getById(id);
	}
	
	@Override
	@PostMapping
	public List<Notification> create(@RequestBody Notification... notifications) {
		return notSrv.create(notifications);
	}
	
	@Override
	@PutMapping
	@Secured("ROLE_USER")
	public List<Notification> update(@RequestBody Notification... notifs) {
		return update(notifs);
	}
	
	@Override
	@DeleteMapping("/{id}")
	public ResponseEntity deleteOne(@PathVariable("id") long id) {
		return ResponseEntity.badRequest().build();
	}
	
	@Override
	@DeleteMapping
	public ResponseEntity delete(@RequestBody Notification... notifications) {
		return ResponseEntity.badRequest().build();
	}
}
