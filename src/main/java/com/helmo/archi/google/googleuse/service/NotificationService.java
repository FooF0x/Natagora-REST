package com.helmo.archi.google.googleuse.service;

import com.helmo.archi.google.googleuse.model.Notification;
import com.helmo.archi.google.googleuse.repository.NotificationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

	private final NotificationRepository notRepo;
	
	public NotificationService(NotificationRepository notRepo) {
		this.notRepo = notRepo;
	}
	
	public List<Notification> getAll() {
		return notRepo.findAll();
	}
}
