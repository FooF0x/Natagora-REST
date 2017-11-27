package com.helmo.archi.google.googleuse.repository;

import com.helmo.archi.google.googleuse.model.Comment;
import com.helmo.archi.google.googleuse.model.Observation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
	
	void deleteAllByObservation(Observation obs);
}
