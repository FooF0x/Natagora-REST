package com.helmo.archi.google.googleuse.repository;

import com.helmo.archi.google.googleuse.model.Notification;
import com.helmo.archi.google.googleuse.model.Observation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
	
	@Modifying
	@Transactional
	@Query(value = "delete from Notification n where n.observation = ?1")
	void deleteAllByObservation(Observation obs);
	
	List<Notification> getByObservation_Id(Long id);
}
