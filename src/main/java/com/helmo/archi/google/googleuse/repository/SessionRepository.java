package com.helmo.archi.google.googleuse.repository;

import com.helmo.archi.google.googleuse.model.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {
	@Query("select s from Session s")
	List<Session> findRange(long one, long two);
}
