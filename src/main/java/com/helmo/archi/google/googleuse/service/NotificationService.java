package com.helmo.archi.google.googleuse.service;

import com.helmo.archi.google.googleuse.model.Notification;
import com.helmo.archi.google.googleuse.model.Observation;
import com.helmo.archi.google.googleuse.repository.NotificationRepository;
import com.helmo.archi.google.googleuse.tools.Time;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class NotificationService implements BasicService<Notification, Long> {
	
	private final NotificationRepository notRepo;
	
	public NotificationService(NotificationRepository notRepo) {
		this.notRepo = notRepo;
	}
	
	@Override
	public List<Notification> getAll() {
		return notRepo.findAll();
	}
	
	@Override
	public Notification getById(Long id) {
		return notRepo.findOne(id);
	}
	
	@Override
	public Notification create(Notification toAdd) {
		Notification notif = new Notification();
		notif.setCaption(toAdd.getCaption());
		notif.setDescription(toAdd.getDescription());
		notif.setDate(toAdd.getDate() != null
			  ? toAdd.getDate()
			  : Time.getTime());
		notif.setStatus(toAdd.isStatus());
		notif.setObservation(toAdd.getObservation());
		return notRepo.save(notif);
	}
	
	@Override
	public Notification update(Notification toUpdate) {
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
	
	@Override
	public void delete(Notification... notifications) {
		notRepo.delete(Arrays.asList(notifications));
	}
	
	@Override
	public void delete(Notification notification) {
		notRepo.delete(notification.getId());
	}
	
	@Override
	public void deleteById(Long id) {
		notRepo.delete(id);
	}
	
	public void deleteByObservation(Observation obs) {
		notRepo.deleteAllByObservation(obs);
	}
}
