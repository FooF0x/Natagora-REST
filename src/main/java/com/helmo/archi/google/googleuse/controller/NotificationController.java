package com.helmo.archi.google.googleuse.controller;

import com.helmo.archi.google.googleuse.model.Notification;
import com.helmo.archi.google.googleuse.service.NotificationService;
import com.helmo.archi.google.googleuse.service.NotificationStatusService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/notifications")
public class NotificationController implements BasicController<Notification> {
	//TODO Get By User and Observation
	private final NotificationService notSrv;
	private final NotificationStatusService statusSrv;
	
	public NotificationController(NotificationService notSrv, NotificationStatusService statusSrv) {
		this.notSrv = notSrv;
		this.statusSrv = statusSrv;
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
	
	@GetMapping("/{one}/{two}")
	@Secured("ROLE_USER")
	public List<Notification> getRange(@PathVariable("one") long one, @PathVariable("two") long two) {
		if (two <= one) throw new IllegalArgumentException("Wrong args");
		return notSrv.getRange(one, two);
	}
	
	@Override
	@PostMapping
	public ResponseEntity create(@RequestBody Notification... notifications) {
		try {
			List<Notification> rtn = new ArrayList<>();
			for (Notification not : notifications) {
				if (not.getStatus().getId() == 0)
					not.setStatus(statusSrv.findByName(not.getStatus().getName()));
				rtn.add(notSrv.create(not));
			}
			return ResponseEntity.ok(rtn);
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}
	
	@Override
	@PutMapping
	@Secured("ROLE_USER")
	public ResponseEntity update(@RequestBody Notification... notifications) {
		try {
			List<Notification> rtn = new ArrayList<>();
			for (Notification not : notifications) {
				if (not.getStatus().getId() == 0)
					not.setStatus(statusSrv.findByName(not.getStatus().getName()));
				rtn.add(notSrv.update(not));
			}
			return ResponseEntity.ok(rtn);
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}
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
