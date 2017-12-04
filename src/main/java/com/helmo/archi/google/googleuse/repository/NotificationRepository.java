package com.helmo.archi.google.googleuse.repository;

import com.helmo.archi.google.googleuse.model.Notification;
import com.helmo.archi.google.googleuse.model.Observation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
	
	@Query("DELETE FROM notifications WHERE observation = :#{#obs.getId()}")
	void deleteAllByObservation(Observation obs);
}
