package com.helmo.archi.google.googleuse.service;

import com.helmo.archi.google.googleuse.model.NotificationStatus;
import com.helmo.archi.google.googleuse.repository.NotificationStatusRepository;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class NotificationStatusService implements BasicService<NotificationStatus, Long> {
	private final NotificationStatusRepository notRep;
	
	public NotificationStatusService(NotificationStatusRepository notSrv) {
		this.notRep = notSrv;
	}
	
	@Override
	public List<NotificationStatus> getAll() {
		return notRep.findAll();
	}
	
	@Override
	public NotificationStatus getById(Long id) {
		return notRep.findOne(id);
	}
	
	@Override
	public NotificationStatus create(NotificationStatus notificationStatus) {
		NotificationStatus temp = new NotificationStatus();
		temp.setName(notificationStatus.getName());
		return notRep.save(temp);
	}
	
	@Override
	public NotificationStatus update(NotificationStatus notificationStatus) {
		return notRep.save(notificationStatus);
	}
	
	@Override
	public void delete(NotificationStatus... notificationStatuses) {
		notRep.delete(Arrays.asList(notificationStatuses));
	}
	
	@Override
	public void delete(NotificationStatus notificationStatus) {
		notRep.delete(notificationStatus);
	}
	
	@Override
	public void deleteById(Long id) {
		notRep.delete(id);
	}
	
	public NotificationStatus findByName(String property) {
		return notRep.findByName(property);
	}
}
