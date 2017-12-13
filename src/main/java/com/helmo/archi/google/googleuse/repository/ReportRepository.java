package com.helmo.archi.google.googleuse.repository;

import com.helmo.archi.google.googleuse.model.Observation;
import com.helmo.archi.google.googleuse.model.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
	
	@Modifying
	@Transactional
	@Query(value = "delete from Report r where r.observation = ?1")
	void deleteAllByObservation(Observation obs);
}
