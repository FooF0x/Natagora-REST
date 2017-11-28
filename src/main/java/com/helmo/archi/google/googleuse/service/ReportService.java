package com.helmo.archi.google.googleuse.service;

import com.helmo.archi.google.googleuse.model.Observation;
import com.helmo.archi.google.googleuse.model.Report;
import com.helmo.archi.google.googleuse.repository.ReportRepository;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Service
public class ReportService {
	
	private final ReportRepository repRepo;
	
	public ReportService(ReportRepository repRepo) {
		this.repRepo = repRepo;
	}
	
	public List<Report> getAll() {
		return repRepo.findAll();
	}
	
	public List<Report> save(Iterable<Report> rpt) {
		return repRepo.save(rpt);
	}
	
	public Report save(Report rpt) {
		Report toSave = new Report();
		toSave.setCommentary(rpt.getCommentary());
		toSave.setDate(
			  (rpt.getDate() != null)
					? rpt.getDate()
					: new Timestamp(new Date().getTime())
		);
		toSave.setObservation(rpt.getObservation());
		toSave.setUser(rpt.getUser());
		return repRepo.save(toSave);
	}
	
	public Report updateOne(Report updated) {
		Report rpt = repRepo.findOne(updated.getId());
		rpt.setCommentary(
			  (updated.getCommentary() != null)
					? updated.getCommentary()
					: rpt.getCommentary()
		);
		return repRepo.save(rpt);
	}
	
	public void deleteOne(Report rpt) {
		repRepo.delete(rpt.getId());
	}
	
	public void deleteByObservation(Observation obs) {
		repRepo.deleteAllByObservation(obs);
	}
}
