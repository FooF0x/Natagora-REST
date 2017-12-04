package com.helmo.archi.google.googleuse.repository;

import com.helmo.archi.google.googleuse.model.Observation;
import com.helmo.archi.google.googleuse.model.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
	
	@Query("DELETE FROM reports WHERE id_observation = :#{#obs.getId()}")
	void deleteAllByObservation(Observation obs);
}
