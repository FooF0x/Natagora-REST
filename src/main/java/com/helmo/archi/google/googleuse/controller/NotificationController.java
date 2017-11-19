package com.helmo.archi.google.googleuse.controller;

import com.helmo.archi.google.googleuse.model.Notification;
import com.helmo.archi.google.googleuse.service.NotificationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/notifications")
public class NotificationController {
	
	private final NotificationService notSrv;
	
	public NotificationController(NotificationService notSrv) {
		this.notSrv = notSrv;
	}
	
	@GetMapping
	public List<Notification> getNotifications() {
		return notSrv.getAll();
	}
}
