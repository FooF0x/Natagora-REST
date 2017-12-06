package com.helmo.archi.google.googleuse.repository;

import com.helmo.archi.google.googleuse.model.Comment;
import com.helmo.archi.google.googleuse.model.Observation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
	
	@Modifying
	@Transactional
	@Query(value = "delete from Comment c where c.observation = ?1")
	void deleteAllByObservation(Observation obs);
}
