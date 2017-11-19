package com.helmo.archi.google.googleuse.repository;

import com.helmo.archi.google.googleuse.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
