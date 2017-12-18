package com.helmo.archi.google.googleuse.repository;

import com.helmo.archi.google.googleuse.model.Session;
import com.helmo.archi.google.googleuse.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {
	List<Session> findByUser(User user);
	
	List<Session> findByUser_Id(Long id);
}
