package com.helmo.archi.google.googleuse.repository;

import com.helmo.archi.google.googleuse.model.Observation;
import com.helmo.archi.google.googleuse.model.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
	
	void deleteAllByObservation(Observation obs);
}
