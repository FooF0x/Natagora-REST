package com.helmo.archi.google.googleuse.repository;

import com.helmo.archi.google.googleuse.model.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {
}
