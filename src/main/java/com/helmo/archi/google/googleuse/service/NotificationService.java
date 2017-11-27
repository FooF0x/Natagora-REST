package com.helmo.archi.google.googleuse.service;

import com.helmo.archi.google.googleuse.model.Notification;
import com.helmo.archi.google.googleuse.model.Observation;
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
	
	public void save(Notification notif) {
		notRepo.save(notif);
	}
	
	public Notification updateOne(Notification toUpdate) {
		Notification notif = notRepo.findOne(toUpdate.getId());
		notif.setStatus(toUpdate.isStatus());
		notif.setCaption(
				toUpdate.getCaption() != null
				? toUpdate.getCaption()
				: notif.getCaption()
		);
		notif.setDescription(
				toUpdate.getDescription() != null
				? toUpdate.getDescription()
				: notif.getDescription()
		);
		notif.setDate(
				toUpdate.getDate() != null
				? toUpdate.getDate()
				: notif.getDate()
		);
		return notRepo.save(notif);
	}
	
	public void deleteByObservation(Observation obs) {
		notRepo.deleteAllByObservation(obs);
	}
}
