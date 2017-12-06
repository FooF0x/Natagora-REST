package com.helmo.archi.google.googleuse.repository;

import com.helmo.archi.google.googleuse.model.NotificationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationStatusRepository extends JpaRepository<NotificationStatus, Long> {
	
	NotificationStatus findByName(String name);
}
