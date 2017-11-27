package com.helmo.archi.google.googleuse.controller;

import com.helmo.archi.google.googleuse.model.Notification;
import com.helmo.archi.google.googleuse.service.NotificationService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notifications")
public class NotificationController {
	
	private final NotificationService notSrv;
	
	public NotificationController(NotificationService notSrv) {
		this.notSrv = notSrv;
	}
	
	@GetMapping
	@Secured("ROLE_USER")
	public List<Notification> getNotifications() {
		return notSrv.getAll();
	}
	
	@PutMapping
	@Secured("ROLE_USER")
	public Notification updateOne(@RequestBody Notification notif) {
		return notSrv.updateOne(notif);
	}
}
